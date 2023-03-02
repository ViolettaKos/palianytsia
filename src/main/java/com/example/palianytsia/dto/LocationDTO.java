package com.example.palianytsia.dto;

import com.example.palianytsia.model.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LocationDTO {

    @NotBlank
    @NotNull
    private City city;
    @NotBlank
    @NotNull
    private String street;
    @NotBlank
    @NotNull
    private String house;
    @NotBlank
    @NotNull
    private int apartment;
    @NotBlank
    @NotNull
    private long id;
}
