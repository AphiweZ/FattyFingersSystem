package com.example.fattyfingers;

public class OrderItem {
    private String name;
    private int price;

    public OrderItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}