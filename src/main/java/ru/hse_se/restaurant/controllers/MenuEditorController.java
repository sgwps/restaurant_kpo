package ru.hse_se.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse_se.restaurant.models.Dish;
import ru.hse_se.restaurant.models.UserRole;
import ru.hse_se.restaurant.repositories.DishRepository;
import ru.hse_se.restaurant.userChecker.UserChecker;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menu_editor")
public class MenuEditorController {
    @Autowired
    DishRepository repository;

    @Autowired
    UserChecker checker;

    @GetMapping
    public String getHTML(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        checker.getUserOr403(userDetails, UserRole.ADMIN);
        List<Dish> dishes = repository.findAll().stream().filter(dish -> dish.isActive()).collect(Collectors.toList());
        model.addAttribute("dishes", dishes);
        return "menu_editor";
    }


    @GetMapping
    @RequestMapping("/delete")
    public RedirectView delete(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String id) {
        checker.getUserOr403(userDetails, UserRole.ADMIN);
        Dish dish = repository.findById(UUID.fromString(id)).get();
        if (dish != null) {
            dish.setActive(false);
            repository.save(dish);
        }
        return new RedirectView("/menu_editor");

    }

}
