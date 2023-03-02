package com.example.palianytsia.service;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.Location;
import com.example.palianytsia.model.User;


public interface UserService {
    void signUp(UserDTO userDTO) throws ServiceException;
    UserDTO findByEmail(String email);
    LocationDTO addAddress(UserDTO userDTO, LocationDTO locationDTO);
    void editAddress(LocationDTO locationDTO);

}
