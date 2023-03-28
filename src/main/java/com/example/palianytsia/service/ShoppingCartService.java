package com.example.palianytsia.service;

import com.example.palianytsia.model.Item;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {
    void addItem(Item item, int quantity);
    void removeItem(Item item);
    void changeQty(Item item, int quantity);
    Map<Item, Integer> getProductsInCart();
    BigDecimal getTotal();
    Integer getQty();
}
