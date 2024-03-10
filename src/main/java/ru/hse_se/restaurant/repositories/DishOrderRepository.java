package ru.hse_se.restaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse_se.restaurant.models.DishOrder;
import ru.hse_se.restaurant.models.Order;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishOrderRepository extends JpaRepository<DishOrder, UUID> {
    List<DishOrder> findByOrder(Order order);

}
