package com.example.palianytsia.repository;

import com.example.palianytsia.model.OrderItemId;
import com.example.palianytsia.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, OrderItemId> {
}
