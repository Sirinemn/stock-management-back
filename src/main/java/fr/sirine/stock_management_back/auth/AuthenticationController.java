package fr.sirine.stock_management_back.auth;

import fr.sirine.stock_management_back.email.EmailService;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.payload.request.ChangePasswordRequest;
import fr.sirine.stock_management_back.payload.request.LoginRequest;
import fr.sirine.stock_management_back.payload.request.RegisterRequest;
import fr.sirine.stock_management_back.payload.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name="Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authenticationService, EmailService emailService) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @Operation(summary = "Authentification utilisateur", description = "Permet à un utilisateur de se connecter avec ses identifiants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentification réussie",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Authentification échouée")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        logger.info("User login request: " + loginRequest);
        return  ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @Operation(summary = "Inscription administrateur", description = "Permet à un administrateur de s'inscrire en premier")
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid RegisterRequest registerRequest) {
        authenticationService.register(registerRequest, "ADMIN");  // Inscrit un ADMIN
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Ajout d'un utilisateur par l'admin", description = "L'admin peut ajouter des utilisateurs")
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        // Inscription de l'utilisateur et récupération des informations
        User newUser = authenticationService.register(registerRequest, "USER");

        // Construction du message contenant les identifiants
        String emailMessage = String.format(
                "Bonjour %s %s,\n\nVotre compte a été créé avec succès.\n\nIdentifiants :\nEmail : %s\nMot de passe : %s\n\nMerci de changer votre mot de passe après connexion.\n\nCordialement,\nL'équipe de gestion des stocks",
                newUser.getFirstname(), newUser.getLastname(), newUser.getEmail(), registerRequest.getPassword()
        );

        // Envoi de l'email
        emailService.sendEmail(newUser.getEmail(), "Inscription réussie", emailMessage);

        return ResponseEntity.accepted().build();
    }
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/users/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ResponseEntity.ok("Mot de passe mis à jour avec succès");
    }


}
