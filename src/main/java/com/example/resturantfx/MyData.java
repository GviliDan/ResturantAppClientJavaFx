package com.example.resturantfx;

import java.util.ArrayList;
import java.util.HashMap;

public class MyData {
    public static HashMap<Integer, Food> myFoodMenu =new HashMap<Integer, Food>();
    public static HashMap<Integer, Drinks> myDrinkMenu =new HashMap<Integer, Drinks>();

    public static ArrayList<Table> activeTables= new ArrayList<Table>();

    public static ArrayList<Table> dailyTables= new ArrayList<Table>();

    public static Integer choosenTable;

    public static Integer totalIncome=0;

    public static Integer monthlyIncome=0;
    public static Integer dailyIncome=0;
    public static Integer dailyTip=0;
}
