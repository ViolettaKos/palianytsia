package com.example.palianytsia.model;

import jakarta.persistence.*;
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
@Entity
@Accessors(chain = true)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalPrice;
    private String deliveryAddress;

    @CreationTimestamp
    private LocalDateTime dateCreated;

//    @ElementCollection
//    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
//    @MapKeyJoinColumn(name = "item_id")
//    @Column(name = "quantity")
//    private Map<Item, Integer> items;
    @OneToMany(mappedBy = "order")
    private List<OrderItems> order_items;

}
