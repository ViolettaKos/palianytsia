package com.example.palianytsia.service;

import com.example.palianytsia.dto.LocationDTO;

public interface LocationService {
    void editAddress(LocationDTO locationDTO);
    void deleteAddress(LocationDTO locationDTO);
    void addAddress(String email, LocationDTO locationDTO);
}
