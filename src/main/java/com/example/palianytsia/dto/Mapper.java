package com.example.palianytsia.dto;

import com.example.palianytsia.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setMobileNumber(user.getMobileNumber())
                .setRoles(new HashSet<>(user.getRoles().stream().map(role -> new ModelMapper()
                        .map(role, RoleDTO.class)).collect(Collectors.toSet())));
    }

}
