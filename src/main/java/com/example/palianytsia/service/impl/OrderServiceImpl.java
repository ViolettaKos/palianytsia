package com.example.palianytsia.service.impl;

import com.example.palianytsia.dto.Mapper;
import com.example.palianytsia.dto.OrderDTO;
import com.example.palianytsia.model.*;
import com.example.palianytsia.repository.OrderItemsRepository;
import com.example.palianytsia.repository.OrderRepository;
import com.example.palianytsia.repository.UserRepository;
import com.example.palianytsia.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemsRepository orderItemsRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            OrderItemsRepository orderItemsRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemsRepository=orderItemsRepository;
    }

    @Transactional
    @Override
    public void putOrder(OrderDTO orderDTO) {
        User user = userRepository.findByEmail(orderDTO.getUserDTO().getEmail());
        Order order = Mapper.toOrder(orderDTO);
        order.setUser(user);

        long order_id=orderRepository.save(order).getId();
        List<OrderItems> orderItems = getOrderItems(orderDTO, order, order_id);
        orderItemsRepository.saveAll(orderItems);
        order.setOrder_items(orderItems);
    }

    private List<OrderItems> getOrderItems(OrderDTO orderDTO, Order order, long order_id) {
        List<OrderItems> orderItems = new ArrayList<>();
        Map<Item, Integer> cartItems = orderDTO.getItems();

        for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
            OrderItemId orderItemId=new OrderItemId();
            orderItemId.setOrder_id(order_id);
            orderItemId.setItem_id(entry.getKey().getId());

            OrderItems orderItem = new OrderItems();
            orderItem.setId(orderItemId);
            orderItem.setOrder(order);
            orderItem.setItem(entry.getKey());
            orderItem.setQuantity(entry.getValue());

            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
