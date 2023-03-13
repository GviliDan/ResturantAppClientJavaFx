package com.example.resturantfx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.resturantfx.MyData.dailyTables;

public class Client implements Runnable {


    public Request getRequest() {
        return request;
    }

    private Request request;

    public Client(Request request) {
        this.request = request;
    }

    public void askFromServer(Request request)  {
        try {

            System.out.println("thread running: "+Thread.currentThread().getName());
            Socket socket = new Socket("localhost", 12345);
            System.out.println("made conection");
//            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);

            String messageFromServer = (String) inputStream.readLine();
            System.out.println("message from server: " + messageFromServer);

            Gson gson=new Gson();
            String requstStr= gson.toJson(request);

            Gson gson1 = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

            outputStream.println(requstStr);
            outputStream.flush();

            messageFromServer = (String) inputStream.readLine();
            System.out.println("message from server: " + messageFromServer);

//            Type type = new TypeToken<HashMap<Integer, Food>>(){}.getType();
//            HashMap<Integer,Food> myData = gson.fromJson(str, type);

            switch (request.getAction()) {
                case "bring food menu": {
                    Type type = new TypeToken<HashMap<Integer, Food>>(){}.getType();
                    MyData.myFoodMenu = gson.fromJson(messageFromServer, type);
                    System.out.println(MyData.myFoodMenu.toString());
                    break;
                }
                case "insert food": {
                    System.out.println("insert done");

                    break;
                }
                case "delete food": {
                    System.out.println("remove done");

                    break;
                }
                case "bring drink menu": {
                    Type type = new TypeToken<HashMap<Integer, Drinks>>(){}.getType();
                    MyData.myDrinkMenu = gson.fromJson(messageFromServer, type);
                    System.out.println(MyData.myDrinkMenu.toString());
                    break;
                }
                case "insert drink": {
                    System.out.println("insert drink done");
                    break;
                }
                case "delete drink": {
                    System.out.println("remove drink done");
                    break;
                }
                case "refresh tables":{
                    Type type = new TypeToken<ArrayList<Table>>() {}.getType();
                    try {
                        MyData.activeTables = gson1.fromJson(messageFromServer, type);
                        System.out.println("tables update");
                    }
                    catch (Exception e){
                        System.out.println("cant change to table array");
                    }
                    break;
                }
                case "update table info": {
                    System.out.println("update done");
                }
                case "close table":{
                    System.out.println("table closed");
                }
                case "calculate income":{
                    MyData.totalIncome= Integer.parseInt(messageFromServer);
                    System.out.println("total Income Updated "+ messageFromServer);
                }
                case "calculate monthly income":{
                    MyData.monthlyIncome= Integer.parseInt(messageFromServer);
                    System.out.println("monthly Income Updated "+ messageFromServer);
                }
                case "calculate daily income":{
                    MyData.dailyIncome= Integer.parseInt(messageFromServer);
                    System.out.println("daily Income Updated "+ messageFromServer);
                }
                case "calculate daily tip":{
                    MyData.dailyTip= Integer.parseInt(messageFromServer);
                    System.out.println("tip Income Updated "+ messageFromServer);
                    break;
                }
                case "check product sales": {
                    if (messageFromServer.equals("no exsist")){
                        ManagementController.countOfSales=-1;
                    }
                    else{
                        ManagementController.countOfSales= Integer.parseInt(messageFromServer);
                        System.out.println("count of product is: "+ messageFromServer);
                    }

                }
                case "caculate incomes":{
                    Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                    ArrayList<Integer> incomes = gson.fromJson(messageFromServer, type);
                    MyData.totalIncome= incomes.get(0);
                    MyData.monthlyIncome= incomes.get(1);
                    MyData.dailyIncome= incomes.get(2);
                    MyData.dailyTip= incomes.get(3);
                    break;
                }
                case "bring daily tabels": {
                    Type type = new TypeToken<ArrayList<Table>>() {}.getType();
                    dailyTables= gson1.fromJson(messageFromServer, type);
                    break;
                }
                default: {

                    break;
                }
            }

            outputStream.close();
            inputStream.close();
            socket.close();
            System.out.println("disconected....");
        }
        catch (Exception e){
            System.out.println("exeption...");
        }

    }

    @Override
    public void run() {
        System.out.println("ask from server");
        this.askFromServer(getRequest());
    }
}
