package com.example.palianytsia.dto;

import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderDTO {
    private String trackingNumber;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private String deliveryAddress;
    @CreationTimestamp
    private LocalDateTime dateCreated;
    private Map<Item, Integer> items;
//    List<OrderItemDTO> itemDTOS;
    private UserDTO userDTO;
}
