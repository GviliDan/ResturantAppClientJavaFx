package com.example.resturantfx;

import java.io.Serializable;

public class Payment implements Serializable {
    private Integer total_amount;
    private Integer total_payment;
    private Integer left_payment;

    private Integer tip;



    public Payment(){
        this.total_amount=0;
        this.total_payment=0;
        this.left_payment=total_amount;
        this.tip=0;

    }
    public Payment(Integer total_amount) {
        this.total_amount = total_amount;
        this.total_payment = 0;
        this.left_payment = total_amount;
        this.tip=0;
    }

    public Integer getTip() {
        return tip;
    }



    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
        this.left_payment=total_amount-this.getTotal_payment();
    }

    public Integer getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(Integer total_payment) {
        this.total_payment = total_payment;
    }

    public Integer getLeft_payment() {
        return left_payment;
    }

    public void setLeft_payment(Integer left_payment) {
        this.left_payment = left_payment;
    }

    public void payBill (Integer amount){
        Integer my_total_payment= this.getTotal_payment();
        Integer my_left_payment= this.getLeft_payment();
        this.setTotal_payment(my_total_payment+amount);
        this.setLeft_payment(my_left_payment-amount);
        this.setTip(0-getLeft_payment());
    }


}
