package com.example.resturantfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PaymentController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label LeftPaymentLabel;

    @FXML
    private Label tableNameLabel;

    @FXML
    private TextField tfAddTipDollar;

    @FXML
    private TextField tfAddTipPrecent;

    @FXML
    private TextField tfPay;

    @FXML
    private Label tipLabel;

    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label totalPaymentLabel;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableNameLabel.setText("Table "+ MyData.choosenTable);
        Payment payment= MyData.activeTables.get(MyData.choosenTable-1).getPayment();
        totalAmountLabel.setText(payment.getTotal_amount() + "$");
        totalPaymentLabel.setText(payment.getTotal_payment()+ "$");

        if (payment.getLeft_payment()>0) {LeftPaymentLabel.setText(payment.getLeft_payment()+ "$");}
        else {LeftPaymentLabel.setText("0$");}

        if (payment.getTip()>0) {tipLabel.setText(payment.getTip() + "$");}
        else {tipLabel.setText(0+"$");}

        if(payment.getLeft_payment()==0){LeftPaymentLabel.setTextFill(Paint.valueOf("GREEN"));}
    }

    public void pay(){
        Integer pay = Integer.parseInt(tfPay.getText());
        if (pay>0) {
            Table table = MyData.activeTables.get(MyData.choosenTable - 1);
            Payment payment = table.getPayment();
            payment.payBill(pay);
            totalPaymentLabel.setText(payment.getTotal_payment() + "$");
            if (payment.getLeft_payment() > 0) {LeftPaymentLabel.setText(payment.getLeft_payment() + "$");}
            else {LeftPaymentLabel.setText("0$");}

            if (payment.getTip()>0) {tipLabel.setText(payment.getTip() + "$");}
            else {tipLabel.setText(0+"$");}

            table.setPayment(payment);
            Thread thread = new Thread(new Client(new Request("update table info", table.tableTojson())));
            Executor executor = Executors.newFixedThreadPool(1);
            executor.execute(thread);
            if (payment.getLeft_payment() == 0) {LeftPaymentLabel.setTextFill(Paint.valueOf("GREEN"));}
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not a valid number");
            alert.showAndWait();
        }

    }

    public void backToOrderPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("order.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();;
    }


    @FXML
    void add_Tip_by_dollar(ActionEvent event) {
        Integer tipByDollar = Integer.valueOf(tfAddTipDollar.getText());
        if (tipByDollar>0) {
            Table table= MyData.activeTables.get(MyData.choosenTable-1);
        Payment payment= table.getPayment();
        if (payment.getLeft_payment()>0) {
            tfPay.setText(String.valueOf(tipByDollar + payment.getLeft_payment()));
        }
        else {
            tfPay.setText(String.valueOf(tipByDollar));
        }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not a valid number");
            alert.showAndWait();
        }
        }

    @FXML
    void add_Tip_by_precent(ActionEvent event) {
        Integer tipByPrecent= Integer.valueOf(tfAddTipPrecent.getText());
        if (tipByPrecent>0) {
            Table table= MyData.activeTables.get(MyData.choosenTable-1);
            Payment payment= table.getPayment();
            if (payment.getLeft_payment() > 0) {
                tfPay.setText(String.valueOf((tipByPrecent * payment.getTotal_amount()) / 100 + payment.getLeft_payment()));
            } else {
                tfPay.setText(String.valueOf((tipByPrecent * payment.getTotal_amount()) / 100));
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not a valid number");
            alert.showAndWait();
        }
    }
}
