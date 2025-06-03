package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.payload.request.ResetPasswordRequest;
import fr.sirine.stock_management_back.payload.response.MessageResponse;
import fr.sirine.stock_management_back.service.impl.PasswordResetTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class PasswordResetController {
    private final PasswordResetTokenService tokenService;

    public PasswordResetController(PasswordResetTokenService tokenService) {
        this.tokenService = tokenService;
    }
    @PostMapping("/reset-password-request")
    public ResponseEntity<MessageResponse> requestResetPassword(@RequestBody ResetPasswordRequest request) {
        tokenService.processPasswordResetRequest(request.getEmail());
        MessageResponse messageResponse = new MessageResponse("Un email de réinitialisation a été envoyé si l’adresse est enregistrée.");
        return ResponseEntity.ok(messageResponse);
    }
}
