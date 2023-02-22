package com.example.palianytsia.repository;

import com.example.palianytsia.model.Role;
import com.example.palianytsia.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(UserRoles role);
}
