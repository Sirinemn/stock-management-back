package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.entities.PasswordResetToken;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.repository.PasswordResetTokenRepository;
import fr.sirine.stock_management_back.service.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;
    private final IUserService userService;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository, IUserService userService) {
        this.tokenRepository = passwordResetTokenRepository;
        this.userService = userService;
    }
    public PasswordResetToken createTokenForUser(String email) {
        User user = userService.findByEmail(email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        return tokenRepository.save(resetToken);
    }
    public Optional<User> validatePasswordResetToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .map(PasswordResetToken::getUser);
    }

    public void deleteToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }
}
