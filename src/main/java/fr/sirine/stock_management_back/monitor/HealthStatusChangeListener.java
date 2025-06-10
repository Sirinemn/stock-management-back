package fr.sirine.stock_management_back.monitor;

import fr.sirine.stock_management_back.auth.AuthenticationController;
import fr.sirine.stock_management_back.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HealthStatusChangeListener {
    private final HealthEndpoint healthEndpoint;
    private final EmailService emailService;
    @Value("${monitor.email}")
    private String monitoringEmail;
    private boolean wasDown = false;
    private static final Logger logger = LoggerFactory.getLogger(HealthStatusChangeListener.class);

    public HealthStatusChangeListener(HealthEndpoint healthEndpoint, EmailService emailService) {
        this.healthEndpoint = healthEndpoint;
        this.emailService = emailService;
    }
    @Scheduled(fixedRate = 60000)
    public void checkHealth() {
        HealthComponent component = healthEndpoint.health();

        if (component instanceof Health health) {
            Map<String, Object> details = health.getDetails(); // ✅ ici getDetails() est OK
            String status = health.getStatus().getCode();

            emailService.sendEmail(
                    monitoringEmail,
                    "StockZen - Un composant est DOWN",
                    "health-alert",
                    Map.of(
                            "status", status,
                            "details", details.toString()  // ou JSON.stringify(details) si besoin
                    )
            );
        } else {
            logger.warn("HealthComponent is not an instance of Health. Cannot extract details.");
        }

    }
    private void sendEmailAlert(Status status, Map<String, Object> details) {
        emailService.sendEmail(
                monitoringEmail,
                "StockZen - Un composant est DOWN",
                "emails/health-alert",
                Map.of("status", status.getCode(), "details", details.toString())
        );
    }

}
