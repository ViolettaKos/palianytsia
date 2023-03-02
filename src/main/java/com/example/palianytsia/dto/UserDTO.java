package com.example.palianytsia.dto;


import com.example.palianytsia.model.Location;
import com.example.palianytsia.model.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDTO {
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    private String repeated_password;
    @NotNull
    @NotBlank
    private String mobileNumber;

    private Collection<RoleDTO> roles;
    @Max(5)
    private Collection<LocationDTO> locations;

}
