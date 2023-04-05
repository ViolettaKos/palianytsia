package com.example.palianytsia.dto;

import com.example.palianytsia.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderItemDTO {
    List<Item> items;
    int quantity;
}
