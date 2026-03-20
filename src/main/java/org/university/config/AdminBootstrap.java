package org.university.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.university.model.AppUser;
import org.university.repo.AppUserRepository;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${uniportal.admin.username}")
    private String adminUsername;

    @Value("${uniportal.admin.password}")
    private String adminPassword;

    public AdminBootstrap(AppUserRepository userRepo,
                          PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        AppUser admin = userRepo.findByUsername(adminUsername).orElse(null);

        if (admin == null) {
            admin = new AppUser(
                    adminUsername,
                    passwordEncoder.encode(adminPassword),
                    "ADMIN"
            );
            userRepo.save(admin);
            System.out.println("✅ Admin user created: " + adminUsername);
        } else {
            // DEV ONLY: force reset admin password so login NEVER mismatches
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole("ADMIN");
            userRepo.save(admin);
            System.out.println("🔁 Admin password reset (DEV): " + adminUsername);
        }
    }
}
