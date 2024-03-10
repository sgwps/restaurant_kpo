package ru.hse_se.restaurant.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse_se.restaurant.repositories.DishOrderRepository;

@Service
public class DishOrderService {
    @Getter
    @Autowired
    private final DishOrderRepository dishOrderRepository;

    public DishOrderService(DishOrderRepository dishOrderRepository) {
        this.dishOrderRepository = dishOrderRepository;
    }
}
