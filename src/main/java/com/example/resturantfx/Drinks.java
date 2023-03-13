package com.example.resturantfx;

public class Drinks extends Order {

    private String barComments;


    public Drinks(Integer price, String name) {
        super();
        this.price= price;
        this.name= name;
        this.barComments="";
    }

    public String getBarComments() {
        return barComments;
    }

    public void setBarComments(String barComments) {
        this.barComments = barComments;
    }




}
