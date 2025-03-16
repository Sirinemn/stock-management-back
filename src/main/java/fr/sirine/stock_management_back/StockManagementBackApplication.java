package fr.sirine.stock_management_back;

import fr.sirine.stock_management_back.entities.Role;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.repository.RoleRepository;
import fr.sirine.stock_management_back.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
@EnableJpaAuditing
public class StockManagementBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockManagementBackApplication.class, args);
	}
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository, UserRepository userRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(Role.builder().name("ADMIN").build());
			}
			if (roleRepository.findByName("CLIENT").isEmpty()) {
				roleRepository.save(Role.builder().name("CLIENT").build());
			}
			if (roleRepository.findByName("SUPPLIER").isEmpty()) {
				roleRepository.save(Role.builder().name("SUPPLIER").build());
			}
			// Initialiser l'utilisateur admin
			if (userRepository.findByEmail("admin@mail.fr").isEmpty()) {
				User admin = new User();
				admin.setFirstname("admin");
				admin.setEmail("admin@mail.fr");
				admin.setLastname("Last");
				admin.setPassword(passwordEncoder().encode("adminpassword"));
				Role adminRole = roleRepository.findByName("ADMIN").orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
				admin.setRoles(new ArrayList<>(Collections.singleton(adminRole)));
				userRepository.save(admin);
			}
		};
	}
}
