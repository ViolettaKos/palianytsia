package com.example.palianytsia.model;

public enum OrderStatus {
    IN_PROGRESS("In progress"), ON_WAY("On a way"), DELIVERED("Delivered");

    private String val;

    OrderStatus(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }

}
