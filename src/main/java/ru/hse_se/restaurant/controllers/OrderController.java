package ru.hse_se.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hse_se.restaurant.models.Order;
import ru.hse_se.restaurant.models.User;
import ru.hse_se.restaurant.models.UserRole;
import ru.hse_se.restaurant.repositories.OrderRepository;
import ru.hse_se.restaurant.userChecker.UserChecker;

import java.util.List;

@Controller
@RequestMapping(path = "orders")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserChecker checker;

    @GetMapping
    public String getHTML(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = checker.getUserOr403(userDetails, UserRole.VISITOR);
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }

}
