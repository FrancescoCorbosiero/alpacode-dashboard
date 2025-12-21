package studio.alpacode.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import studio.alpacode.app.repository.UserRepository;

@Service
public class AdminSeederService {

    private static final Logger log = LoggerFactory.getLogger(AdminSeederService.class);

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${app.admin.email:admin@alpacode.studio}")
    private String adminEmail;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Value("${app.admin.name:Administrator}")
    private String adminName;

    public AdminSeederService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedAdminUser() {
        if (!userRepository.existsAdmin()) {
            log.info("========================================");
            log.info("No admin user found. Creating default admin...");
            log.info("========================================");

            userService.createAdmin(adminEmail, adminPassword, adminName);

            log.info("Admin user created successfully!");
            log.info("Email: {}", adminEmail);
            log.info("Password: {}", adminPassword);
            log.info("========================================");
            log.info("IMPORTANTE: Cambia la password di default in produzione!");
            log.info("========================================");
        } else {
            log.info("Admin user already exists. Skipping seeding.");
        }
    }
}
