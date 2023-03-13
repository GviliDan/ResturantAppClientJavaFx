package com.example.resturantfx;

import java.io.Serializable;

public class TablesView implements Serializable {

    private Integer tableId;
    private Integer tableNum;
    private Integer totalAmount;
    private Integer num_of_diners;
    private String tableOpenTime;
    private Integer tip;

    public TablesView(Integer tableId, Integer tableNum, Integer totalAmount, Integer num_of_diners, String tableOpenTime, Integer tip) {
        this.tableId = tableId;
        this.tableNum = tableNum;
        this.totalAmount = totalAmount;
        this.num_of_diners = num_of_diners;
        this.tableOpenTime = tableOpenTime;
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "TablesView{" +
                "tableId=" + tableId +
                ", tableNum=" + tableNum +
                ", totalAmount=" + totalAmount +
                ", num_of_diners=" + num_of_diners +
                ", tableOpenTime='" + tableOpenTime + '\'' +
                ", tip=" + tip +
                '}';
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getTableNum() {
        return tableNum;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNum_of_diners() {
        return num_of_diners;
    }

    public void setNum_of_diners(Integer num_of_diners) {
        this.num_of_diners = num_of_diners;
    }

    public String getTableOpenTime() {
        return tableOpenTime;
    }

    public void setTableOpenTime(String tableOpenTime) {
        this.tableOpenTime = tableOpenTime;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }
}

