package ru.hse_se.restaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse_se.restaurant.models.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String name);
}

