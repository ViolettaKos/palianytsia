package com.example.palianytsia.service.impl;

import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.ItemType;
import com.example.palianytsia.repository.ItemRepository;
import com.example.palianytsia.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {


    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl (ItemRepository itemRepository) {
        this.itemRepository=itemRepository;
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findItemById(id);
    }

    @Override
    public Page<Item> displayAllItems(List<ItemType> selectedTypes, Pageable pageable) {
        Page<Item> itemPage;
        if(!selectedTypes.isEmpty()) {
            itemPage=itemRepository.findItemsByItemTypeIn(selectedTypes, pageable);
        } else
            itemPage=itemRepository.findAll(pageable);
        return itemPage;
    }

    @Override
    public Page<Item> displayCookies(Pageable pageable) {
        return itemRepository.findItemsByItemType(ItemType.COOKIE, pageable);
    }

    @Override
    public List<Item> displayCakes() {
        return itemRepository.findItemsByItemType(ItemType.CAKE);
    }

    @Override
    public List<Item> displayCroissants() {
        return itemRepository.findItemsByItemType(ItemType.CROISSANT);
    }

    @Override
    public List<Item> displayCupcakes() {
        return itemRepository.findItemsByItemType(ItemType.CUPCAKE);
    }

    @Override
    public List<Item> displayCheesecakes() {
        return itemRepository.findItemsByItemType(ItemType.CHEESECAKE);
    }

    @Override
    public Page<Item> displayThreeCookies() {
        return itemRepository.findItemsByItemType(ItemType.COOKIE, PageRequest.of(0,3));
    }

    @Override
    public Page<Item> displayThreeCakes() {
        return itemRepository.findItemsByItemType(ItemType.CAKE, PageRequest.of(0,3));
    }

    @Override
    public Page<Item> displayThreeCroissants() {
        return itemRepository.findItemsByItemType(ItemType.CROISSANT, PageRequest.of(0,3));
    }

    @Override
    public Page<Item> displayThreeCupcakes() {
        return itemRepository.findItemsByItemType(ItemType.CUPCAKE, PageRequest.of(0,3));
    }

    @Override
    public Page<Item> displayThreeCheesecakes() {
        return itemRepository.findItemsByItemType(ItemType.CHEESECAKE, PageRequest.of(0,3));
    }
}
