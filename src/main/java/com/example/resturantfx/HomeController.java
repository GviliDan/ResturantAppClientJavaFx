package com.example.resturantfx;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button btnTable1;

    @FXML
    private Button btnTable11;

    @FXML
    private Button btnTable111;

    @FXML
    private Button btnTable112;

    @FXML
    private Button btnTable12;

    @FXML
    private Button btnTable121;

    @FXML
    private Button btnTable122;

    @FXML
    private Button btnTable13;

    @FXML
    private Button btnTable14;

    @FXML
    private GridPane grid;

    @FXML
    private Rectangle reka;
    @FXML
    private Label s1;

    @FXML
    private Label s2;

    @FXML
    private Label s3;

    @FXML
    private Label s4;

    @FXML
    private Label s5;

    @FXML
    private Label s6;

    @FXML
    private Label s7;

    @FXML
    private Label s8;

    @FXML
    private Label s9;
    @FXML
    private Button managementBut;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label[] labels = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
//        System.out.println(labels[1]);
        for( int i=0; i<9; i++){
            if (MyData.activeTables.get(i).getStatus()){
                labels[i].setText(" Active");
                labels[i].setTextFill(Paint.valueOf("GREEN"));
            }
            else {
                labels[i].setText(" Empty");
                labels[i].setTextFill(Paint.valueOf("ORANGE"));
            }
        }
    }

    public void switchToOrderPage(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        MyData.choosenTable=  Integer.parseInt(buttonText);
//        System.out.println("Button text: " + buttonText);
//        System.out.println("***  " + event);

        Thread thread1 = new Thread(new Client(new Request("bring food menu")));
        Thread thread2 = new Thread(new Client(new Request("bring drink menu")));
        Thread thread3= new Thread(new Client(new Request("refresh tables")));
        Executor executor = Executors.newFixedThreadPool(3);
        executor.execute(thread1);
        executor.execute(thread2);
        executor.execute(thread3);

        System.out.println("** check: "+ MyData.activeTables.get(MyData.choosenTable).getStatus());

        if ((MyData.activeTables.get(MyData.choosenTable-1).getStatus())==false){
            askOpen(event);
        }
        else {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("order.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }

    }

    public void askOpen(ActionEvent event){
        // start
        TextInputDialog dialog = new TextInputDialog();

        // Set the header text and content text for the dialog
        dialog.setHeaderText("Do you want to open this table ? ");
        dialog.setContentText("Please enter the number of dinners:");

        // Show the dialog and wait for the user to input an integer value
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                Integer numDinners = Integer.parseInt(result.get());
                if(numDinners<1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "number of dinners is not valid.");
                    alert.showAndWait();
//                    askOpen(event);
                    return;
                }
                else {
                    MyData.activeTables.get(MyData.choosenTable-1).setNum_of_diners(numDinners);
                    MyData.activeTables.get(MyData.choosenTable-1).setTableOpenTime(LocalDateTime.now());
                    MyData.activeTables.get(MyData.choosenTable-1).setStatus(true);
                    Thread thread= new Thread(new Client(new Request("update table info",MyData.activeTables.get(MyData.choosenTable-1).tableTojson())));
                    Executor executor = Executors.newFixedThreadPool(1);
                    executor.execute(thread);
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("order.fxml"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }



            } catch (NumberFormatException e) {
                // User input was not a valid integer
                // Handle the error here
            }
        }

        //end
    }

    public void switchToFoodMenuPage(ActionEvent event) throws IOException, InterruptedException {
        Thread thread = new Thread(new Client(new Request("bring food menu")));
        thread.start();
        thread.join();
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(new Client(new Request("bring food menu")));
//        executor.shutdown();
        Parent root = FXMLLoader.load(getClass().getResource("menu_food.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDrinksMenuPage(ActionEvent event) throws IOException {
        Thread thread = new Thread(new Client(new Request("bring drink menu")));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Parent root = FXMLLoader.load(getClass().getResource("menu_drink.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ;
    }

    @FXML
    void switchToManagementPage(ActionEvent event) throws IOException, InterruptedException {

        Thread thread =new Thread(new Client(new Request("caculate incomes")));
        thread.start();
        thread.join();
        Thread thread1= new Thread(new Client(new Request("bring daily tabels")));
        thread1.start();
        thread1.join();


        Parent root = FXMLLoader.load(getClass().getResource("management.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}



