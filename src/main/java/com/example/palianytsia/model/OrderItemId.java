package com.example.palianytsia.model;

import jakarta.persistence.Embeddable;
import org.aspectj.weaver.ast.Or;

import java.io.Serializable;

@Embeddable
public class OrderItemId implements Serializable {

    private long order_id;
    private long item_id;
}
