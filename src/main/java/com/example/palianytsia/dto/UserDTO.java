package com.example.palianytsia.dto;


import com.example.palianytsia.model.Notifications;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDTO {

    @NotBlank(message = "{constraints.NotEmpty.message}")
    private String firstName;

    @NotBlank(message = "{constraints.NotEmpty.message}")
    private String lastName;

    @NotBlank(message = "{constraints.NotEmpty.message}")
    private String email;


    private String password;


    private String repeated_password;
    private String newPass;
    private String confirmPass;

    @NotBlank(message = "{constraints.NotEmpty.message}")
    private String mobileNumber;

    private Set<RoleDTO> roles;

    private Collection<LocationDTO> locations;

    private Notifications notifications;

}
