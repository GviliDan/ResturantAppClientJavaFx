package com.example.resturantfx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.resturantfx.MyData.dailyTables;


public class ManagementController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label amountOfSalesLabel;

    @FXML
    private Label dailyLabel;

    @FXML
    private Label monhlyLabel;

    @FXML
    private Button searchBut;

    @FXML
    private Label tableNameLabel;

    @FXML
    private TableView<TablesView> tableTV;

    @FXML
    private TextField tfProduvt;

    @FXML
    private Label tipLabel;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Button backHomeBut;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label totalPaymentLabel11;

    @FXML
    private TableColumn<TablesView, Integer> num_of_diners;

    @FXML
    private TableColumn<TablesView, Integer> tableId;


    @FXML
    private TableColumn<TablesView, Integer> tableNum;

    @FXML
    private TableColumn<TablesView, String> tableOpenTime;


    @FXML
    private TableColumn<TablesView, Integer> tip;

    @FXML
    private TableColumn<TablesView, Integer> totalAmount;



    public static Integer countOfSales=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        totalAmountLabel.setText(MyData.totalIncome.toString());
        monhlyLabel.setText((MyData.monthlyIncome.toString()));
        dailyLabel.setText(MyData.dailyIncome.toString());
        tipLabel.setText(MyData.dailyTip.toString());


        tableId.setCellValueFactory(new PropertyValueFactory<TablesView, Integer>("tableId"));
        num_of_diners.setCellValueFactory(new PropertyValueFactory<TablesView, Integer>("num_of_diners"));
        tableNum.setCellValueFactory(new PropertyValueFactory<TablesView, Integer>("tableNum"));
        tableOpenTime.setCellValueFactory(new PropertyValueFactory<TablesView, String>("tableOpenTime"));
        tip.setCellValueFactory(new PropertyValueFactory<TablesView, Integer>("tip"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<TablesView, Integer>("totalAmount"));



        ObservableList<TablesView> tables =  tableTV.getItems();
        for (int i=0; i<dailyTables.size();i++)
        {
            Integer tableId= dailyTables.get(i).getTableId();
            Integer tableNum=dailyTables.get(i).getTableNum();
            Integer totalAmount= dailyTables.get(i).getPayment().getTotal_amount();
            Integer num_of_diners= dailyTables.get(i).getNum_of_diners();
            String tableOpenTime= dailyTables.get(i).getTableOpenTime().format(DateTimeFormatter.ofPattern("dd/MM   HH:mm"));
            Integer tip=  dailyTables.get(i).getPayment().getTip();

            tables.add(new TablesView(tableId, tableNum, totalAmount, num_of_diners, tableOpenTime, tip));
        }

        tableTV.setItems(tables);

    }


    @FXML
    void backToHomePage(ActionEvent event) throws IOException {
        Thread thread= new Thread(new Client(new Request("refresh tables")));
        Executor executor = Executors.newFixedThreadPool(1);
        executor.execute(thread);
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }

    @FXML
    void checkProductSales(ActionEvent event) throws InterruptedException {
        String product= tfProduvt.getText();
        if ((product!=null)&&(!product.isEmpty())){
            Thread thread=new Thread(new Client(new Request("check product sales", product)));
//            Executor executor= Executors.newSingleThreadExecutor();
//            executor.execute(thread);
            thread.start();
            thread.join();
            if (countOfSales!=-1){
                amountOfSalesLabel.setText(product+ " sales: "+ countOfSales);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, product+ " is not exsist in menu");
                alert.showAndWait();
                amountOfSalesLabel.setText("--");
            }


        }
    }

    @FXML
    void datePickerFunc(ActionEvent event) {
        LocalDate dateChossen = datePicker.getValue();
        tableTV.getItems().clear();
        ObservableList<TablesView> tables =  tableTV.getItems();
        for (int i=0; i<dailyTables.size();i++) {
            if (dailyTables.get(i).getTableOpenTime().getDayOfMonth() == dateChossen.getDayOfMonth() &&
                    dailyTables.get(i).getTableOpenTime().getYear() == dateChossen.getYear()) {
                Integer tableId = dailyTables.get(i).getTableId();
                Integer tableNum = dailyTables.get(i).getTableNum();
                Integer totalAmount = dailyTables.get(i).getPayment().getTotal_amount();
                Integer num_of_diners = dailyTables.get(i).getNum_of_diners();
                String tableOpenTime = dailyTables.get(i).getTableOpenTime().format(DateTimeFormatter.ofPattern("dd/MM   HH:mm"));
                Integer tip = dailyTables.get(i).getPayment().getTip();

                tables.add(new TablesView(tableId, tableNum, totalAmount, num_of_diners, tableOpenTime, tip));
            }
            }

            tableTV.setItems(tables);
    }


    @FXML
    void goToOrderMan(ActionEvent event) throws IOException {
//        Thread thread= new Thread(new Client(new Request("refresh tables")));
//        Executor executor = Executors.newFixedThreadPool(1);
//        executor.execute(thread);
        Parent root = FXMLLoader.load(getClass().getResource("order_management.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;

    }

}
