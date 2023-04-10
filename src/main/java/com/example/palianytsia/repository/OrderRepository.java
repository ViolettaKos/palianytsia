package com.example.palianytsia.repository;

import com.example.palianytsia.model.Order;
import com.example.palianytsia.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //    List<Order> findByUserOrderByDateCreated(User user);
    Page<Order> findAllByUser(User user, Pageable pageable);
}
