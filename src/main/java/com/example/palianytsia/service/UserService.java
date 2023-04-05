package com.example.palianytsia.service;

import com.example.palianytsia.dto.OrderDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.ServiceException;

import java.util.List;
import java.util.Set;


public interface UserService {
    void signUp(UserDTO userDTO) throws ServiceException;
    UserDTO findByEmail(String email);
    UserDTO updateProfile(String email, UserDTO userDTO) throws ServiceException;
    void updatePassword(UserDTO userDTO);
    List<OrderDTO> findOrders(String email);
}
