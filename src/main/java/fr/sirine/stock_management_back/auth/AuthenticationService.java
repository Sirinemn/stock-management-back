package fr.sirine.stock_management_back.auth;

import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.jwt.JwtService;
import fr.sirine.stock_management_back.payload.request.LoginRequest;
import fr.sirine.stock_management_back.payload.request.RegisterRequest;
import fr.sirine.stock_management_back.payload.response.AuthenticationResponse;
import fr.sirine.stock_management_back.repository.RoleRepository;
import fr.sirine.stock_management_back.entities.Role;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequest request) {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = ((User) auth.getPrincipal());
        var jwtToken = jwtService.generateToken((User) auth.getPrincipal());

        return AuthenticationResponse.builder()
                .userId(user.getId())
                .token(jwtToken)
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
