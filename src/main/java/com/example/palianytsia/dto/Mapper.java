package com.example.palianytsia.dto;

import com.example.palianytsia.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Mapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setMobileNumber(user.getMobileNumber())
                .setRoles(new HashSet<>(user.getRoles().stream().map(role -> new ModelMapper()
                        .map(role, RoleDTO.class)).collect(Collectors.toSet())))
                .setLocations(new HashSet<>(user.getLocations().stream().map(location -> new ModelMapper()
                        .map(location, LocationDTO.class)).collect(Collectors.toSet())));
    }

    // #todo think about locations, null pointer when register
    public static User toUser(UserDTO userDTO) {
        User user = new User()
                .setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName())
                .setEmail(userDTO.getEmail())
                .setMobileNumber(userDTO.getMobileNumber())
                .setRoles(new HashSet<>(userDTO.getRoles().stream().map(role -> new ModelMapper()
                        .map(role, Role.class)).collect(Collectors.toSet())));

        if (userDTO.getLocations() != null) {
            user.setLocations(new HashSet<>(userDTO.getLocations().stream().map(location -> new ModelMapper()
                    .map(location, Location.class)).collect(Collectors.toSet())));
        }
        return user;
    }

    public static LocationDTO toLocationDTO(Location location) {
        return new LocationDTO()
                .setId(location.getId())
                .setCity(location.getCity())
                .setStreet(location.getStreet())
                .setHouse(location.getHouse())
                .setApartment(location.getApartment());
    }

    public static Location toLocation(LocationDTO locationDTO) {
        return new Location()
                .setCity(locationDTO.getCity())
                .setStreet(locationDTO.getStreet())
                .setHouse(locationDTO.getHouse())
                .setApartment(locationDTO.getApartment());
    }

    public static Order toOrder(OrderDTO orderDTO) {
        return new Order()
                .setTrackingNumber(orderDTO.getTrackingNumber())
                .setStatus(orderDTO.getOrderStatus())
                .setDeliveryAddress(orderDTO.getDeliveryAddress())
                .setTotalPrice(orderDTO.getTotalPrice());

    }

    public static OrderDTO toOrderDTO(Order order) {
        List<OrderItems> orderItemsList = order.getOrder_items();
        Map<Item, Integer> items = orderItemsList.stream()
                .collect(Collectors.toMap(OrderItems::getItem, OrderItems::getQuantity));
        return new OrderDTO()
                .setDateCreated(order.getDateCreated())
                .setOrderStatus(order.getStatus())
                .setTotalPrice(order.getTotalPrice())
                .setTrackingNumber(order.getTrackingNumber())
                .setDeliveryAddress(order.getDeliveryAddress())
                .setItems(items);
    }

}
