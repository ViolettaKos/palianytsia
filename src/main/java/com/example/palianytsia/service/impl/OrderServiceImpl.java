package com.example.palianytsia.service.impl;

import com.example.palianytsia.dto.Mapper;
import com.example.palianytsia.dto.OrderDTO;
import com.example.palianytsia.model.Order;
import com.example.palianytsia.model.User;
import com.example.palianytsia.repository.OrderRepository;
import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository=orderRepository;
        this.userRepository=userRepository;
    }
    @Override
    public void putOrder(OrderDTO orderDTO) {
        User user=userRepository.findByEmail(orderDTO.getUserDTO().getEmail());
        Order order= Mapper.toOrder(orderDTO);
        order.setUser(user);
        orderRepository.save(order);
    }
}
