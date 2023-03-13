package com.example.resturantfx;

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
import java.util.ResourceBundle;

import static com.example.resturantfx.MyData.dailyTables;

public class OrderManagementController implements Initializable {


    @FXML
    private Button backToHomePagebtn;

    @FXML
    private Button backToManagmentPagebtn;
    @FXML
    private TableColumn<OrderView, String> name;

    @FXML
    private TableColumn<OrderView,Integer> price;

    @FXML
    private TableView<OrderView> resrevation;

    @FXML
    private TextField tfID;

    @FXML
    private Label totalAmountLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    void search(ActionEvent event) {
        resrevation.getItems().clear();
        int idSelected = Integer.parseInt(tfID.getText());
        ObservableList<OrderView> orderViews =  resrevation.getItems();

        for (int i=0; i<dailyTables.size();i++)
        {
            if (dailyTables.get(i).getTableId()==idSelected)
            {
                for (int j = 0; j < dailyTables.get(i).getFood_order().size(); j++) {
                    String tableNam = dailyTables.get(i).getFood_order().get(j).getName();
                    Integer totalAmount = dailyTables.get(i).getFood_order().get(j).getPrice();
                    orderViews.add(new OrderView(213, tableNam, totalAmount));

                }
                resrevation.setItems(orderViews);
                for (int j = 0; j < dailyTables.get(i).getDrinks_order().size(); j++) {
                    String tableNam = dailyTables.get(i).getDrinks_order().get(j).getName();
                    Integer totalAmount = dailyTables.get(i).getDrinks_order().get(j).getPrice();
                    orderViews.add(new OrderView(213, tableNam, totalAmount));

                }
                resrevation.setItems(orderViews);
                totalAmountLabel.setText(dailyTables.get(i).getPayment().getTotal_amount()+"$");
                break;
            }
        }

    }

    private Stage stage;
    private Scene scene;

    @FXML
    void backToHomePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void backToManagmentPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("management.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }

}
