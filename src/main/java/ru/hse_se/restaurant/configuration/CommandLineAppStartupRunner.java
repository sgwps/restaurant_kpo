package ru.hse_se.restaurant.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.hse_se.restaurant.models.User;
import ru.hse_se.restaurant.models.UserRole;
import ru.hse_se.restaurant.repositories.UserRepository;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String...args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("visitor") == null) {
            User visitor = new User();
            visitor.setUsername("visitor");
            visitor.setPassword(encoder.encode("visitor"));
            visitor.setRole(UserRole.VISITOR);
            userRepository.save(visitor);
        }
    }


}