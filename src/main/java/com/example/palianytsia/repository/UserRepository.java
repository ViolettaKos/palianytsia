package com.example.palianytsia.repository;

import com.example.palianytsia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
