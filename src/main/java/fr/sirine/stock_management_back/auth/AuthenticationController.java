package fr.sirine.stock_management_back.auth;

import fr.sirine.stock_management_back.payload.request.LoginRequest;
import fr.sirine.stock_management_back.payload.request.RegisterRequest;
import fr.sirine.stock_management_back.payload.response.AuthenticationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return  ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
        return ResponseEntity.accepted().build();
    }
}
