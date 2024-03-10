package ru.hse_se.restaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse_se.restaurant.models.Dish;

import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
}
