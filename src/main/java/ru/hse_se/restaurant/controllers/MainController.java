package ru.hse_se.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse_se.restaurant.models.User;
import ru.hse_se.restaurant.models.UserRole;
import ru.hse_se.restaurant.repositories.UserRepository;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    UserRepository repository;

    @GetMapping
    public RedirectView getHTML(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new RedirectView("/login");
        }
        User user = repository.findByUsername(userDetails.getUsername());
        if (user.getRole() == UserRole.ADMIN) {
            return new RedirectView("/menu_editor");
        }
        return new RedirectView("/orders");
    }
}
