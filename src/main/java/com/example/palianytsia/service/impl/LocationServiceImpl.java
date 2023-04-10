package com.example.palianytsia.service.impl;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.Mapper;
import com.example.palianytsia.model.Location;
import com.example.palianytsia.model.User;
import com.example.palianytsia.repository.LocationRepository;
import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.LocationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    private LocationRepository locationRepository;
    private UserRepository userRepository;

    public LocationServiceImpl(LocationRepository locationRepository, UserRepository userRepository) {
        super();
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void editAddress(LocationDTO locationDTO) {
        Location location = locationRepository.findLocationById(locationDTO.getId());
        User user = location.getUser();
        Long id = location.getId();
        location = Mapper.toLocation(locationDTO);
        location.setUser(user);
        location.setId(id);

        locationRepository.save(location);
    }

    @Override
    public void deleteAddress(LocationDTO locationDTO) {
        Location location = locationRepository.findLocationById(locationDTO.getId());
        log.info("id: " + location.getId());
        locationRepository.deleteById(location.getId());
    }

    @Override
    public void addAddress(String email, LocationDTO locationDTO) {
        User user = userRepository.findByEmail(email);
        log.info("locationDto id: " + locationDTO.getId());
        Location location = Mapper.toLocation(locationDTO);
        location.setUser(user);

        locationRepository.save(location);
    }

    @Override
    public LocationDTO findById(Long id) {
        return Mapper.toLocationDTO(locationRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }
}
