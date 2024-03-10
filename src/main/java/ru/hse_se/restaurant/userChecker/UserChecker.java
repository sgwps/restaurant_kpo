package ru.hse_se.restaurant.userChecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.hse_se.restaurant.models.User;
import ru.hse_se.restaurant.models.UserRole;
import ru.hse_se.restaurant.repositories.UserRepository;

@Configuration
public class UserChecker {
    @Autowired
    UserRepository userRepository;

    public User getUserOr403(UserDetails userDetails, UserRole role) {
        if (userDetails == null) {
            throw new AccessDeniedException("");
        }
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user.getRole() != role) {
            throw new AccessDeniedException("");
        }
        return user;
    }
}