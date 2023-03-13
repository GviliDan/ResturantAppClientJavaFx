package com.example.resturantfx;

import com.google.gson.Gson;
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
import java.util.ResourceBundle;

public class MenuFoodController implements Initializable {


    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private TableColumn<OrderView, Integer> id;

    @FXML
    private TableColumn<OrderView, String> name;

    @FXML
    private TableColumn<OrderView, Integer> price;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPrice;

    @FXML
    private TableView<OrderView> tvFood;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("initialize...");
        id.setCellValueFactory(new PropertyValueFactory<OrderView, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<OrderView,String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<OrderView,Integer>("price"));


        ObservableList<OrderView> foods =  tvFood.getItems();
        for (Integer key:MyData.myFoodMenu.keySet())
        {
//            System.out.println(key+ " "+MyData.myFoodMenu.get(key).getName()+" "+MyData.myFoodMenu.get(key).getPrice()  );
            foods.add(new OrderView(key,MyData.myFoodMenu.get(key).getName(), MyData.myFoodMenu.get(key).getPrice()));
//            foods.add(food);
        }
//        System.out.println(foods.toString());

        tvFood.setItems(foods);

    }


    @FXML
    void insert(ActionEvent event)
    {
        // Get the text from the name and price text fields
        String  keyId = tfId.getText().trim();
        String foodName = tfName.getText().trim();
        String foodPriceText = tfPrice.getText().trim();

        // Validate input
        if (foodName.isEmpty() || foodPriceText.isEmpty() || keyId.isEmpty()) {
            // Show an error message if either text field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter id, name and price for the food.");
            alert.showAndWait();
            return;
        }

        Integer foodPrice;
        try {
            // Try to parse the drink price as an integer
            foodPrice = Integer.parseInt(foodPriceText);
        } catch (NumberFormatException e) {
            // Show an error message if the drink price is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid integer for the food price.");
            alert.showAndWait();
            return;
        }
        // Create a new food object with the given name and price
        OrderView food = new OrderView(Integer.parseInt(keyId), foodName, foodPrice);
        Food addFood = new Food(foodPrice,foodName);
        // Add the food object to the table view
        ObservableList<OrderView> foods = tvFood.getItems();

        foods.add(food);
        MyData.myFoodMenu.put(Integer.parseInt(keyId),addFood);

        tvFood.setItems(foods);


        // Clear the text fields
        tfId.clear();
        tfName.clear();
        tfPrice.clear();
        Gson gson=new Gson();
        Thread thread = new Thread(new Client(new Request("insert food",gson.toJson(addFood),keyId)));
        thread.start();
    }

    @FXML
    void delete(ActionEvent event){
        Integer selectedID = tvFood.getSelectionModel().getSelectedIndex();
        if (selectedID==-1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Choose item to delete.");
            alert.showAndWait();
            return;
            }
        System.out.println(tvFood.getItems().get(selectedID));
        String keyDelete= tvFood.getItems().get(selectedID).getId().toString();
        System.out.println("delete key: "+keyDelete);
        MyData.myDrinkMenu.remove(tvFood.getItems().get(selectedID).getId());
        Gson gson=new Gson();
        Thread thread = new Thread(new Client(new Request("delete food",keyDelete)));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tvFood.getItems().remove(selectedID);
//        System.out.println(tvFood.getItems().get(selectedID));
//        String keyDelete= tvFood.getItems().get(selectedID).getId().toString();
//        System.out.println("delete key: "+keyDelete);
//        MyData.myFoodMenu.remove(tvFood.getItems().get(selectedID).getId());
//        Gson gson=new Gson();
//        Thread thread = new Thread(new Client(new Request("delete food",keyDelete)));
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        tvFood.getItems().remove(selectedID);



    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void backToHomePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }

}
