package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.UserDto;
import fr.sirine.stock_management_back.email.EmailService;
import fr.sirine.stock_management_back.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        String subject = "Alert: Stock level below threshold";

        List<UserDto> admins = getAdminsToNotify();
        for (UserDto admin : admins) {
            Map<String, Object> variables = Map.of(
                    "adminName", admin.getFirstname() + " " + admin.getLastname(),
                    "productName", product.getName(),
                    "quantity", product.getQuantity()
            );
            emailService.sendEmail(admin.getEmail(), subject, "stock_alert", variables );
        }
    }

    private List<UserDto> getAdminsToNotify() {
        return userService.findByRole("ADMIN");
    }

}
