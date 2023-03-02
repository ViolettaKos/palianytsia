package com.example.palianytsia.service.impl;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.Mapper;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.*;
import com.example.palianytsia.model.Location;
import com.example.palianytsia.model.Role;
import com.example.palianytsia.model.User;
import com.example.palianytsia.model.UserRoles;
import com.example.palianytsia.repository.LocationRepository;
import com.example.palianytsia.repository.RoleRepository;
import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.palianytsia.dto.Mapper.toUserDTO;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private LocationRepository locationRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                           LocationRepository locationRepository) {
        super();
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.locationRepository=locationRepository;
    }

    @Override
    public void signUp(UserDTO userDTO) throws ServiceException {
        User user=userRepository.findByEmail(userDTO.getEmail());
        if (user==null) {
            log.info("No such user!");

//            Role role;
//            role=roleRepository.findByRole(UserRoles.USER);
//            user=new User()
//                    .setFirstName(userDTO.getFirstName())
//                    .setLastName(userDTO.getLastName())
//                    .setEmail(userDTO.getEmail())
//                    .setPassword(passwordEncoder.encode(userDTO.getPassword()))
//                    .setMobileNumber(userDTO.getMobileNumber())
//                    .setRoles(new HashSet<>(Arrays.asList(role)));
            user=Mapper.toUser(userDTO);

            log.info("Name of user: "+user.getFirstName());
            log.info("User role size: "+user.getRoles().size());
            user.getRoles().forEach(t -> System.out.println("role: "+t.getRole().name()));
            userRepository.save(user);
        } else {
        log.info("User already exists");
        throw new DuplicatedEmailException(); }
    }

    @Override
    public UserDTO findByEmail(String email) {
        return toUserDTO(userRepository.findByEmail(email));
    }

    @Override
    public LocationDTO addAddress(UserDTO userDTO, LocationDTO locationDTO) {
        User user=userRepository.findByEmail(userDTO.getEmail());
        log.info("user name: "+user.getFirstName());
        Location location= Mapper.toLocation(locationDTO);
        location.setUser(user);
//        locationRepository.save(location);
//        Location location=new Location()
//                .setUser(user)
//                .setCity(locationDTO.getCity())
//                .setStreet(locationDTO.getStreet())
//                .setHouse(locationDTO.getHouse())
//                .setApartment(locationDTO.getApartment());
        Set<Location> locations = user.getLocations();
        if (locations == null) {
            locations = new HashSet<>();
        }
        locations.add(location);
        user.setLocations(locations);

        userRepository.save(user);
        log.info("id location: "+location.getId());
        locationDTO.setId(location.getId());
        return locationDTO;
    }

    @Override
    public void editAddress(LocationDTO locationDTO) {
        Location location=locationRepository.findLocationById(locationDTO.getId());
        User user=location.getUser();
        Long id=location.getId();
        location=Mapper.toLocation(locationDTO);
        location.setUser(user);
        location.setId(id);

        locationRepository.save(location);
    }


}
