package com.example.resturantfx;

public class DataHolder {
    private static DataHolder instance = new DataHolder();
    private int totalPrice;

    private DataHolder() {}

    public static DataHolder getInstance() {
        return instance;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
