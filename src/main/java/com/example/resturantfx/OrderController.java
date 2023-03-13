package com.example.resturantfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OrderController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML
    private Button AddDrinkBut;

    @FXML
    private Button addFoodBut;

    @FXML
    private Button backHomeBut;
    @FXML
    private Button closeBut;

    @FXML
    private Button deleteBut;


    @FXML
    private Label startTV;

    @FXML
    private Label tableIdTV;

    @FXML
    private TextField tfNumOfDiner;

    @FXML
    private Button payBut;
    @FXML
    private Label myLable;

    @FXML
    private Label totalPriceLabel;

    @FXML
    ChoiceBox<OrderView> myChoiceBoxFood = new ChoiceBox<>();
    @FXML
    ChoiceBox<OrderView> myChoiceBoxDrinks = new ChoiceBox<>();


    @FXML
    private Label lableNumOfDiner;

    @FXML
    private TableView<OrderView> resrevation   = new TableView<>();

    @FXML
    private TableColumn<OrderView, Integer> id;

    @FXML
    private TableColumn<OrderView, String> name;

    @FXML
    private TableColumn<OrderView,Integer> price;

    @FXML
    private Label tableNameLabel;


    private Integer totalPrice=0;






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableNameLabel.setText("Table "+ MyData.choosenTable);
        tableIdTV.setText(MyData.activeTables.get(MyData.choosenTable-1).getTableId().toString());
        lableNumOfDiner.setText(MyData.activeTables.get(MyData.choosenTable-1).getNum_of_diners().toString());
        startTV.setText(MyData.activeTables.get(MyData.choosenTable-1).getTableOpenTime().format(DateTimeFormatter.ofPattern("dd/MM   HH:mm")));
//        totalPrice= MyData.activeTables.get(MyData.choosenTable-1).getPayment().getTotal_amount();

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

//        ObservableList<OrderView> data = FXCollections.observableArrayList();
//        ObservableList<OrderView> data = resrevation.getItems();
//        resrevation.setItems(data);

        ObservableList<OrderView> orderViews =  resrevation.getItems();
        for (Drinks drink1: MyData.activeTables.get(MyData.choosenTable-1).getDrinks_order())
        {
            orderViews.add(new OrderView(2,drink1.getName(), drink1.getPrice()));
            totalPrice+=drink1.getPrice();
        }


        for (Food food1: MyData.activeTables.get(MyData.choosenTable-1).getFood_order())
        {
            orderViews.add(new OrderView(1,food1.getName(), food1.getPrice()));
            totalPrice+=food1.getPrice();
        }

        resrevation.setItems(orderViews);
        totalPriceLabel.setText(totalPrice.toString()+ " $");



//        resrevation.getColumns().addAll(id, name, price);


        // choise box defintion
        ArrayList<OrderView> food_list = new ArrayList<>();
        for (Integer key:MyData.myFoodMenu.keySet())
        {
            food_list.add(new OrderView(key,MyData.myFoodMenu.get(key).getName(), MyData.myFoodMenu.get(key).getPrice()));
        }

        myChoiceBoxFood.setItems(FXCollections.observableArrayList(food_list));

        ArrayList<OrderView> drink_list = new ArrayList<>();
        for (Integer key:MyData.myDrinkMenu.keySet())
        {
            drink_list.add(new OrderView(key,MyData.myDrinkMenu.get(key).getName(), MyData.myDrinkMenu.get(key).getPrice()));
        }

        myChoiceBoxDrinks.setItems(FXCollections.observableArrayList(drink_list));

        ObservableList<Integer> numbers = FXCollections.observableArrayList(1, 2, 3, 4, 5,6,7,8,9,10,11,12);

    }

    @FXML
    void delete(ActionEvent event){
        int selectedID = resrevation.getSelectionModel().getSelectedIndex();
        OrderView order = resrevation.getItems().get(selectedID);

        Table table= MyData.activeTables.get(MyData.choosenTable-1);
        Payment payment=table.getPayment();
        if(order.getId()==1) {
            Food food= new Food(order.getPrice(),order.getName());
            ArrayList<Food> food_list = MyData.activeTables.get(MyData.choosenTable-1).getFood_order();
//            System.out.println("food to delete: "+ food.getName()+ " size="+food_list.size() );
            for (int i=0; i<food_list.size(); i++){
                if (food_list.get(i).getName().equals(food.getName()) && (food_list.get(i).getPrice().equals(food.getPrice()))){
                    food_list.remove(i);
//                    System.out.println("food list: "+ food_list.toString());
                    break;
                }
            }
            payment.setTotal_amount(payment.getTotal_amount()-food.getPrice());
            table.setFood_order(food_list);
            table.setPayment(payment);
        }
        else {
            Drinks drink= new Drinks(order.getPrice(),order.getName());
            ArrayList<Drinks> drink_list = MyData.activeTables.get(MyData.choosenTable-1).getDrinks_order();
            for (int i=0; i<drink_list.size(); i++){
                if (drink_list.get(i).getName().equals(drink.getName()) && (drink_list.get(i).getPrice().equals(drink.getPrice()))){
                    drink_list.remove(i);
                    break;
                }
            }
            MyData.activeTables.set(MyData.choosenTable-1, table);
            payment.setTotal_amount(payment.getTotal_amount()-drink.getPrice());
            table.setDrinks_order(drink_list);
            table.setPayment(payment);
        }
        Thread thread= new Thread(new Client(new Request("update table info",table.tableTojson())));
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(thread);

        totalPrice =totalPrice- order.getPrice();
        resrevation.getItems().remove(selectedID);
        totalPriceLabel.setText( totalPrice+ " $");
    }

    public void backToHomePage(ActionEvent event) throws IOException {
        Thread thread= new Thread(new Client(new Request("refresh tables")));
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(thread);
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }

    public void addFood(ActionEvent event){
        int selectedID = myChoiceBoxFood.getSelectionModel().getSelectedIndex();
        OrderView choosen = myChoiceBoxFood.getItems().get(selectedID);
        ObservableList<OrderView> data = resrevation.getItems();
        choosen.setId(1);
        data.add(choosen);
        resrevation.setItems(data);
        totalPrice += choosen.getPrice();
        totalPriceLabel.setText(totalPrice+ " $");
//        Payment payment=new Payment(totalPrice);
        Payment payment=MyData.activeTables.get(MyData.choosenTable-1).getPayment();
        payment.setTotal_amount(payment.getTotal_amount()+choosen.getPrice());
        ArrayList<Food> food_list= MyData.activeTables.get(MyData.choosenTable-1).getFood_order();
        food_list.add(new Food(myChoiceBoxFood.getItems().get(selectedID).getPrice(),myChoiceBoxFood.getItems().get(selectedID).getName()));
        MyData.activeTables.get(MyData.choosenTable-1).setFood_order(food_list);
        MyData.activeTables.get(MyData.choosenTable-1).setPayment(payment);
        Thread thread= new Thread(new Client(new Request("update table info",MyData.activeTables.get(MyData.choosenTable-1).tableTojson())));
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(thread);

    }
    public void addDrink(ActionEvent event){
        int selectedID = myChoiceBoxDrinks.getSelectionModel().getSelectedIndex();
        OrderView choosen = myChoiceBoxDrinks.getItems().get(selectedID);
        ObservableList<OrderView> data = resrevation.getItems();
        choosen.setId(2);
        data.add(choosen);
        resrevation.setItems(data);
        totalPrice += choosen.getPrice();
        totalPriceLabel.setText(totalPrice+ " $");
        Payment payment=MyData.activeTables.get(MyData.choosenTable-1).getPayment();
        payment.setTotal_amount(payment.getTotal_amount()+choosen.getPrice());
        ArrayList<Drinks> drink_list= MyData.activeTables.get(MyData.choosenTable-1).getDrinks_order();
        drink_list.add(new Drinks(myChoiceBoxDrinks.getItems().get(selectedID).getPrice(),myChoiceBoxDrinks.getItems().get(selectedID).getName()));
        MyData.activeTables.get(MyData.choosenTable-1).setDrinks_order(drink_list);
        Thread thread= new Thread(new Client(new Request("update table info",MyData.activeTables.get(MyData.choosenTable-1).tableTojson())));
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(thread);

    }

    public void switchToPaymentPage(ActionEvent event) throws IOException {
        DataHolder.getInstance().setTotalPrice(totalPrice);
        Parent root = FXMLLoader.load(getClass().getResource("payment.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeTable(ActionEvent event) throws IOException {
        Table table= MyData.activeTables.get(MyData.choosenTable-1);
        Payment payment= table.getPayment();
        if (payment.getLeft_payment()<=0){
            Thread thread= new Thread(new Client(new Request("close table",table.tableTojson())));
            Thread thread1=new Thread(new Client(new Request("refresh tables",table.tableTojson())));
            MyData.activeTables.get(MyData.choosenTable-1).setStatus(false);
            Executor executor = Executors.newFixedThreadPool(1);
            executor.execute(thread);
            executor.execute(thread1);
            backToHomePage(event);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "still left payment exsist");
            alert.showAndWait();
        }


    }


}
