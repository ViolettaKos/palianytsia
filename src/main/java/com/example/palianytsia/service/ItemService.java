package com.example.palianytsia.service;

import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    Item findById(Long id);

    Page<Item> displayAllItems(List<ItemType> selectedTypes, Pageable pageable);

    Page<Item> displayCookies(Pageable pageable);

    List<Item> displayCakes();

    List<Item> displayCroissants();

    List<Item> displayCupcakes();

    List<Item> displayCheesecakes();

    Page<Item> displayThreeCookies();

    Page<Item> displayThreeCakes();

    Page<Item> displayThreeCroissants();

    Page<Item> displayThreeCupcakes();

    Page<Item> displayThreeCheesecakes();
}
