package com.example.palianytsia.service.impl;

import com.example.palianytsia.model.Item;
import com.example.palianytsia.repository.ItemRepository;
import com.example.palianytsia.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ItemRepository itemRepository;
    private Map<Item, Integer> items=new HashMap<>();

    @Autowired
    public ShoppingCartServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public void addItem(Item item, int quantity) {
        log.info("Amount to  add: "+quantity);
        if(items.containsKey(item)) {
            log.info("Product already in cart, incrementing...");
            items.replace(item, items.get(item)+quantity);
        } else {
            log.info("No such product added so adding a new one");
            items.put(item, quantity);
        }
    }

    @Override
    public void removeItem(Item item) {
        if(items.containsKey(item)) {
            items.remove(item);
        } else {
            log.info("No such product to remove!");
        }
    }

    @Override
    public void changeQty(Item item, int qty) {
        items.replace(item, qty);
//        if (items.containsKey(item)) {
//            items.put(item, qty);
//        }
    }

    @Override
    public Map<Item, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(items);
    }

    @Override
    public BigDecimal getTotal() {
        return items.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Integer getQty() {
        return items.entrySet().stream()
                .map(Map.Entry::getValue).reduce(Integer::sum).orElse(0);
    }
}
