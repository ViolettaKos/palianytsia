package com.example.palianytsia.dto;

import com.example.palianytsia.model.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoleDTO {

    private Long id;
    private UserRoles role;
}
