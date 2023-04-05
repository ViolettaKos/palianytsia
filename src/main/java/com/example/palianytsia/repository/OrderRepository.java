package com.example.palianytsia.repository;

import com.example.palianytsia.model.Order;
import com.example.palianytsia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findByUser(User user);
    List<Order> findByUserOrderByDateCreated(User user);
}
