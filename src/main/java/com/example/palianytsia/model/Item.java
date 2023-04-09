package com.example.palianytsia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false)
    private long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "item_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String name;

    @Column(name = "imgurl",nullable = false)
    private String imgURL;

}
