package com.example.resturantfx;

import java.io.Serializable;

public class Order implements Serializable {

    String name;
    Integer price;


    public Order(Integer price,String name) {
        this.price = price;
        this.name = name;
    }

    public Order() {

    }
    public String getOrderName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name;
    }
}
