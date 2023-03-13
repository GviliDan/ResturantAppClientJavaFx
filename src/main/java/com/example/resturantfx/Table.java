package com.example.resturantfx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Table implements Serializable {

    public static final AtomicInteger  IdForTables =new AtomicInteger(0);
    private Integer tableId;

    Integer tableNum;

    Boolean status;
    private Integer totalAmount;
    private Integer num_of_diners;
    private ArrayList<Food> food_order;
    private ArrayList<Drinks> drinks_order;

    private LocalDateTime tableOpenTime;
//    private LocalTime endTime;
    private  Payment payment;



// function getters and setters
    public Table() {
        this.num_of_diners = 0;
        this.tableId=IdForTables.getAndIncrement();
        this.food_order=new ArrayList<Food>();
        this.drinks_order=new ArrayList<Drinks>();
        this.tableOpenTime= LocalDateTime.now();
        this.payment=new Payment();
        this.totalAmount=0;
        this.tableNum= -1;
        this.status=false;

    }
    public Table(Integer num_of_diners, Integer tableNum, Boolean status) {
        this.num_of_diners = num_of_diners;
        this.tableId=IdForTables.getAndIncrement();
        this.tableNum=tableNum;
        this.food_order=new ArrayList<Food>();
        this.drinks_order=new ArrayList<Drinks>();
        this.tableOpenTime= LocalDateTime.now();
        this.payment=new Payment();
        this.totalAmount=0;
        this.status=status;
    }
    public void setFood_order(ArrayList<Food> food_order) {
        this.food_order = food_order;
    }

    public void setDrinks_order(ArrayList<Drinks> drinks_order) {
        this.drinks_order = drinks_order;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public void setTableOpenTime(LocalDateTime tableOpenTime) {
        this.tableOpenTime = tableOpenTime;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Integer getTableNum() {
        return tableNum;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getTableId() {
        return tableId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public Integer getNum_of_diners() {
        return num_of_diners;
    }

    public ArrayList<Food> getFood_order() {
        return food_order;
    }

    public ArrayList<Drinks> getDrinks_order() {
        return drinks_order;
    }


    public LocalDateTime getTableOpenTime() {
        return tableOpenTime;
    }

    public Payment getPayment() {
        return payment;
    }


    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setNum_of_diners(Integer num_of_diners) {
        this.num_of_diners = num_of_diners;
    }


    // *************************************************************
    // my functions :

    public void inviteFood(Food food){
        this.food_order.add(food);
        this.setTotalAmount(this.getTotalAmount()+food.getPrice());
        this.payment.setTotal_amount(this.getTotalAmount());
    }

    public void inviteDrink(Drinks drink) {
        this.drinks_order.add(drink);
        this.setTotalAmount(this.getTotalAmount()+drink.getPrice());
        this.payment.setTotal_amount(this.getTotalAmount());
    }

    public void printBill(){
        System.out.println("food: ");
        for (Food food: this.getFood_order()) {
            System.out.println(food.getOrderName()+" "+food.getPrice());
        }
        System.out.println("drinks:");
        for (Drinks drinks: this.getDrinks_order()) {
            System.out.println(drinks.getOrderName()+" "+drinks.getPrice());
        }
        System.out.println("total amount: "+ this.getTotalAmount());
        System.out.println("paid :"+this.getPayment().getTotal_payment());
        System.out.println("left to pay: "+this.getPayment().getLeft_payment());
    }



    public String tableTojson(){
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

        String json= ((Gson) gson).toJson(this);
        return json;
    }


    public static Table tableFromJson(String json){
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        return ((Gson) gson).fromJson(json, Table.class);
    }


}
