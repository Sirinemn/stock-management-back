package fr.sirine.stock_management_back.auth;

import fr.sirine.stock_management_back.email.EmailService;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.EmailAlreadyUsedException;
import fr.sirine.stock_management_back.exceptions.custom.RoleNotFoundException;
import fr.sirine.stock_management_back.jwt.JwtService;
import fr.sirine.stock_management_back.payload.request.ChangePasswordRequest;
import fr.sirine.stock_management_back.payload.request.LoginRequest;
import fr.sirine.stock_management_back.payload.request.RegisterRequest;
import fr.sirine.stock_management_back.payload.response.AuthenticationResponse;
import fr.sirine.stock_management_back.repository.RoleRepository;
import fr.sirine.stock_management_back.entities.Role;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService, PasswordEncoder passwordEncoder, EmailService emailService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }

    public User register(RegisterRequest request, String role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Utiliser le mot de passe saisi par l'admin
        user.setRoles(List.of(roleRepository.findByName(role).orElseThrow(RoleNotFoundException::new)));

        // Appliquer firstLogin uniquement pour les utilisateurs normaux (USER)
        if ("USER".equalsIgnoreCase(role)) {
            user.setFirstLogin(true);
        }

        userRepository.save(user);

        // 📧 Envoyer un email de confirmation d'inscription
        String emailMessage = String.format(
                "Bonjour %s,\n\nVotre compte a été créé avec succès.\n\nIdentifiants :\nEmail : %s\n\nMerci de vous connecter et de changer votre mot de passe dès votre première connexion.\n\nCordialement,\nL'équipe de gestion des stocks",
                user.getFirstname() + " " + user.getLastname(), user.getEmail()
        );

        emailService.sendEmail(user.getEmail(), "Inscription réussie", emailMessage);

        return user;
    }


    public AuthenticationResponse authenticate(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String userEmail = ((UserDetails) auth.getPrincipal()).getUsername();
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .userId(user.getId())
                .token(jwtToken)
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }
    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false); // Désactiver le mode "première connexion"
        userRepository.save(user);
    }

}
