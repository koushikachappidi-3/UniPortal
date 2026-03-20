package org.university.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.university.model.AppUser;
import org.university.repo.AppUserRepository;
import org.university.model.Course;
import org.university.repo.CourseRepository;


@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(AppUserRepository userRepo,
                               PasswordEncoder encoder,
                               CourseRepository courseRepo) {
        return args -> {

            if (userRepo.findByUsername("student1").isEmpty()) {
                userRepo.save(new AppUser("student1",
                        encoder.encode("pass123"),
                        "STUDENT"));
            }

            if (userRepo.findByUsername("admin1").isEmpty()) {
                userRepo.save(new AppUser("admin1",
                        encoder.encode("adminpass"),
                        "ADMIN"));
            }

            if (courseRepo.count() == 0) {
                courseRepo.save(new Course("CS612", "Web Services", "Prof. X"));
                courseRepo.save(new Course("CS632P", "Parallel Computing", "Prof. Y"));
            }
        };
    }
}

