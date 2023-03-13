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

public class MenuDrinkController implements Initializable {


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
    private TableView<OrderView> tvDrink;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("initialize...");
        id.setCellValueFactory(new PropertyValueFactory<OrderView, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<OrderView,String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<OrderView,Integer>("price"));


        ObservableList<OrderView> drinks =  tvDrink.getItems();
        for (Integer key:MyData.myDrinkMenu.keySet())
        {
            drinks.add(new OrderView(key,MyData.myDrinkMenu.get(key).getName(), MyData.myDrinkMenu.get(key).getPrice()));
        }

        tvDrink.setItems(drinks);

    }


    @FXML
    void insert(ActionEvent event)
    {
        // Get the text from the name and price text fields
        String  keyId = tfId.getText().trim();
        String drinkName = tfName.getText().trim();
        String drinkPriceText = tfPrice.getText().trim();

        // Validate input
        if (drinkName.isEmpty() || drinkPriceText.isEmpty() || keyId.isEmpty()) {
            // Show an error message if either text field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter id, name and price for the drink.");
            alert.showAndWait();
            return;
        }

        Integer drinkPrice;
        try {
            // Try to parse the drink price as an integer
            drinkPrice = Integer.parseInt(drinkPriceText);
        } catch (NumberFormatException e) {
            // Show an error message if the drink price is not a valid integer
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid integer for the drink price.");
            alert.showAndWait();
            return;
        }
        // Create a new drink object with the given name and price
        OrderView drink = new OrderView(Integer.parseInt(keyId), drinkName, drinkPrice);
        Drinks addDrink = new Drinks(drinkPrice,drinkName);
        // Add the drink object to the table view
        ObservableList<OrderView> drinks = tvDrink.getItems();

        drinks.add(drink);
        MyData.myDrinkMenu.put(Integer.parseInt(keyId),addDrink);

        tvDrink.setItems(drinks);


        // Clear the text fields
        tfId.clear();
        tfName.clear();
        tfPrice.clear();
        Gson gson=new Gson();
        Thread thread = new Thread(new Client(new Request("insert drink",gson.toJson(addDrink),keyId)));
        thread.start();
    }

    @FXML
    void delete(ActionEvent event){
        int selectedID = tvDrink.getSelectionModel().getSelectedIndex();
        if (selectedID==-1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Choose item to delete.");
            alert.showAndWait();
            return;
        }
        System.out.println(tvDrink.getItems().get(selectedID));
        String keyDelete= tvDrink.getItems().get(selectedID).getId().toString();
        System.out.println("delete key: "+keyDelete);
        MyData.myDrinkMenu.remove(tvDrink.getItems().get(selectedID).getId());
        Gson gson=new Gson();
        Thread thread = new Thread(new Client(new Request("delete drink",keyDelete)));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tvDrink.getItems().remove(selectedID);
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
