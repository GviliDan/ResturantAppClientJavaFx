package com.example.resturantfx;

import java.io.Serializable;

public class Food extends Order implements Serializable {

    public Food(Integer price, String name) {
        super();
        this.price= price;
        this.name= name;
    }


    public Food() {
        super();

    }

    public String toString(){
        return "price= "+getPrice()+" name= "+getName()+" " ;
    }

}
