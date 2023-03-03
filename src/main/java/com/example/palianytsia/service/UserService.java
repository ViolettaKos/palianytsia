package com.example.palianytsia.service;

import com.example.palianytsia.dto.LocationDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.ServiceException;


public interface UserService {
    void signUp(UserDTO userDTO) throws ServiceException;
    UserDTO findByEmail(String email);
}
