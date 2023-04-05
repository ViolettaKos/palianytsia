package com.example.palianytsia.repository;

import com.example.palianytsia.model.Order;
import com.example.palianytsia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
