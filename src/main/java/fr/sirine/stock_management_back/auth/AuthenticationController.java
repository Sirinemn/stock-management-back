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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(AuthenticationService authenticationService, EmailService emailService) {
        this.authenticationService = authenticationService;
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
        authenticationService.registerAdmin(registerRequest);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Ajout d'un utilisateur par l'admin", description = "L'admin peut ajouter des utilisateurs")
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        User admin = authenticationService.getAuthenticatedUser(); // Récupération de l'admin connecté
        authenticationService.register(registerRequest, "USER", admin);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/users/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ResponseEntity.ok("Mot de passe mis à jour avec succès");
    }

}
