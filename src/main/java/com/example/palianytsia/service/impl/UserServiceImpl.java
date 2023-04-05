package com.example.palianytsia.service.impl;

import com.example.palianytsia.dto.Mapper;
import com.example.palianytsia.dto.OrderDTO;
import com.example.palianytsia.dto.UserDTO;
import com.example.palianytsia.exception.DuplicatedEmailException;
import com.example.palianytsia.exception.ServiceException;
import com.example.palianytsia.model.Order;
import com.example.palianytsia.model.User;
import com.example.palianytsia.repository.OrderRepository;
import com.example.palianytsia.repository.RoleRepository;
import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.palianytsia.dto.Mapper.toUserDTO;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.orderRepository=orderRepository;
    }

    @Override
    public void signUp(UserDTO userDTO) throws ServiceException {
        User user=userRepository.findByEmail(userDTO.getEmail());
        if (user==null) {
            log.info("No such user!");
            user=Mapper.toUser(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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
    public UserDTO updateProfile(String email, UserDTO userDTO) throws ServiceException {
        User user=userRepository.findByEmail(email);
        if(!email.equals(userDTO.getEmail())) {
            log.info("Not same email");
            if(userRepository.findByEmail(userDTO.getEmail())!=null)
                throw new DuplicatedEmailException();
        }
        log.info("Same email");
        user.setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName())
                .setMobileNumber(userDTO.getMobileNumber())
                .setEmail(userDTO.getEmail());
        userRepository.save(user);
        return Mapper.toUserDTO(user);
    }

    @Override
    public void updatePassword(UserDTO userDTO) {
        User user=userRepository.findByEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getNewPass()));
        userRepository.save(user);
    }

    @Override
    public List<OrderDTO> findOrders(String email) {
        User user=userRepository.findByEmail(email);
        List<Order> orders=orderRepository.findByUserOrderByDateCreated(user);
        return orders.stream().map(Mapper::toOrderDTO).collect(Collectors.toList());
    }
}
