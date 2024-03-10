package ru.hse_se.restaurant.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import ru.hse_se.restaurant.models.*;
import ru.hse_se.restaurant.repositories.DishOrderRepository;
import ru.hse_se.restaurant.repositories.DishRepository;
import ru.hse_se.restaurant.repositories.OrderRepository;
import ru.hse_se.restaurant.userChecker.UserChecker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/create_order")
@SessionAttributes("orderForm")
public class OrderCreatorController {
    @Autowired
    DishOrderRepository dishOrderRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    UserChecker checker;

    @ModelAttribute("orderForm")
    public OrderForm getForm() {
        return new OrderForm();
    }

    private OrderStatus getOrderStatus(Order order) {
        if (order.getId() == null)
            return OrderStatus.NOT_CREATED;
        if (order.getReadyTime().isAfter(LocalDateTime.now())) {
            return OrderStatus.PREPARING;
        }
        if (!order.is_paid()) return OrderStatus.READY;
        return OrderStatus.PAID;
    }

    private DishOrder createDishOrder(Dish dish) {
        DishOrder dishOrder = new DishOrder();
        dishOrder.setDish(dish);
        return dishOrder;
    }

    public Order getOrder(String id, UserDetails userDetails) {
        User user = checker.getUserOr403(userDetails, UserRole.VISITOR);
        Order order;
        if (id == null) {
            order = new Order();
            order.setUser(user);
        }
        else {
            order = orderRepository.findById(UUID.fromString(id)).orElse(new Order());
            if (order.getUser() != null && !order.getUser().getId().equals(user.getId()) ) {
                throw new AccessDeniedException("");
            }
        }
        return order;
    }

    @GetMapping
    public String getHTML(Model model, @ModelAttribute("orderForm") OrderForm orderForm, @RequestParam(required = false) String id, @AuthenticationPrincipal UserDetails userDetails) {
        List<Dish> dishes = dishRepository.findAll();
        List<DishOrder> dishOrders = dishes.stream().filter(dish -> dish.isActive()).map(dish -> createDishOrder(dish)).collect(Collectors.toList());
        orderForm.setDishOrders(dishOrders);
        Order order = getOrder(id, userDetails);
        if (order.getId() != null) {
            List<DishOrder> existing = dishOrderRepository.findByOrder(order);
            dishOrders.stream().forEach(dishOrder -> {
                if (!existing.stream().anyMatch(existingDishOrder -> existingDishOrder.getDish().getId().equals(dishOrder.getDish().getId()))) {
                    existing.add(dishOrder);
                }
            });
            orderForm.setDishOrders(existing);
            orderForm.setStatus(getOrderStatus(order));
        }
        if (order.getId() != null)
            orderForm.setOrderId(order.getId().toString());
        model.addAttribute("orderForm", orderForm);
        return "order_creator";
    }

    @PostMapping
    public RedirectView resolvePostRequest(Model model, @ModelAttribute("orderForm") OrderForm orderForm, @RequestParam(required = false) String id, BindingResult result, @AuthenticationPrincipal UserDetails userDetails)  {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
        try {
            Order order = getOrder(id, userDetails);
            int max_time = 0;
            List<DishOrder> dishOrders = new ArrayList<>();
            for (DishOrder dishOrder : orderForm.getDishOrders()) {
                if (dishOrder.getCount() != 0) {
                    int prev_count = 0;
                    try {
                        UUID dishOrderId = dishOrder.getId();
                        prev_count = dishOrderRepository.findById(dishOrderId).get().getCount();
                    } catch (Exception e) {
                        prev_count = 0;
                    }
                    dishOrder.setDish(dishRepository.getById(dishOrder.getDish().getId()));

                    if (prev_count < dishOrder.getCount()) {
                        max_time = Integer.max(dishOrder.getDish().getCookingTimeMinutes(), max_time);
                    }
                    dishOrder.setOrder(order);
                    dishOrders.add(dishOrder);
                }
            }
            LocalDateTime time = LocalDateTime.now().plusMinutes(max_time);
            if (order.getReadyTime().isBefore(time) || (dishOrders.size() != 0 && order.getId() == null)) {
                order.setReadyTime(time);
                orderRepository.save(order);
            }
            for (DishOrder dishOrder : dishOrders) {
                dishOrder.setOrder(order);
                dishOrderRepository.save(dishOrder);
            }

            return new RedirectView("/orders");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));

        }
    }


    @GetMapping
    @RequestMapping("/pay")
    public RedirectView pay(@RequestParam(required = false) String id, @AuthenticationPrincipal UserDetails userDetails) {
        Order order = getOrder(id, userDetails);
        if (id != null) {
            order.set_paid(true);
            orderRepository.save(order);
        }
        else {
            id = "";
        }
        return new RedirectView("/create_order?id=" + id);

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    class OrderForm{
        List<DishOrder> dishOrders;
        OrderStatus status = OrderStatus.NOT_CREATED;
        String orderId;

        public boolean isPreparing() {
            return status == OrderStatus.PREPARING;
        }
        public boolean isNotCreated() {
            return status == OrderStatus.NOT_CREATED;
        }
        public boolean isNotReady() {return this.isPreparing() || this.isNotCreated();}
        public boolean isReady() {
            return status == OrderStatus.READY;
        }
        public boolean isPaid() {
            return status == OrderStatus.PAID;
        }

        public int sum() {
            int result = 0;
            for (DishOrder dishOrder : dishOrders) {
                result += dishOrder.getDish().getPrice() * dishOrder.getCount();
            }
            return result;
        }
    }
}
