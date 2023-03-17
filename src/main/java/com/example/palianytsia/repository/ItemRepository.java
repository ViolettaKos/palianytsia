package com.example.palianytsia.repository;

import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);

    Page<Item> findItemsByItemType(@Param("item_type") ItemType itemType, Pageable pageable);
    List<Item> findItemsByItemType(ItemType itemType);

}
