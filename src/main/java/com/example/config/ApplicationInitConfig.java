package com.example.config;

import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Initializing application.....");

        return args -> {
            if (userRepository.findUserByEmail(System.getenv("ADMIN_USER_NAME"))==null) {
                User user = new User();
                user.setUserName(System.getenv("ADMIN_USER_NAME"));
                user.setPassword(passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")));
                user.setRole(UserRole.ADMIN);
                userRepository.save(user);
                log.warn("Admin has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
