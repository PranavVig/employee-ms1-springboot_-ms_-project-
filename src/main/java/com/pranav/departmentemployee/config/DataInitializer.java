package com.pranav.departmentemployee.config;

import com.pranav.departmentemployee.entity.User;
import com.pranav.departmentemployee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createUserIfNotExists("employee", "employee123", "ROLE_EMPLOYEE");
        createUserIfNotExists("manager", "manager123", "ROLE_MANAGER");
        createUserIfNotExists("admin", "admin123", "ROLE_ADMIN");
    }

    private void createUserIfNotExists(String username,
                                       String password,
                                       String role) {

        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .enabled(true)
                .build();

        userRepository.save(user);
    }
}