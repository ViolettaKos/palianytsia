package com.example.palianytsia.service;

import com.example.palianytsia.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    List<Item> displayAllItems();
    Page<Item> displayCookies(Pageable pageable);
    List<Item> displayCakes();
    List<Item> displayCroissants();
    List<Item> displayCupcakes();
    List<Item> displayCheesecakes();

    Page<Item> displayThreeCookies();
}
