package ru.hse_se.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse_se.restaurant.models.Dish;
import ru.hse_se.restaurant.models.UserRole;
import ru.hse_se.restaurant.repositories.DishRepository;
import ru.hse_se.restaurant.userChecker.UserChecker;

@Controller
@RequestMapping("/create_dish")
public class DishCreatorController {
    @Autowired
    DishRepository repository;

    @Autowired
    UserChecker checker;

    @GetMapping
    public String getHTML(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        checker.getUserOr403(userDetails, UserRole.ADMIN);
        model.addAttribute("dish", new Dish());
        return "dish_creator";
    }


    @PostMapping
    public RedirectView resolvePostRequest(@AuthenticationPrincipal UserDetails userDetails, Model model, @ModelAttribute Dish dish, BindingResult result)  {
        checker.getUserOr403(userDetails, UserRole.ADMIN);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
        try {
            repository.save(dish);
            return new RedirectView("/menu_editor");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
    }
}