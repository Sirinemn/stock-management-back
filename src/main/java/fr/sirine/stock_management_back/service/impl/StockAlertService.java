package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockAlertService {
    private final EmailService emailService;

    private final UserService userService;

    public StockAlertService(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }
    public void checkStockLevel(Product product) {
        if (product.getQuantity() <= product.getThreshold()) {
            sendStockAlert(product);
        }
    }

    private void sendStockAlert(Product product) {
        String subject = "Alerte : Stock faible pour " + product.getName();
        String message = "Le produit '" + product.getName() + "' est en stock faible ! "
                + "Quantité restante : " + product.getQuantity();

        List<UserDto> admins = getAdminsToNotify();
        for (UserDto admin : admins) {
            emailService.sendEmail(admin.getEmail(), subject, message);
        }
    }

    private List<UserDto> getAdminsToNotify() {
        return userService.getAllUsers().stream()
                .filter(user -> user.getRoles().contains("ADMIN"))
                .collect(Collectors.toList());
    }

}
