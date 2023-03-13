package com.example.resturantfx;

public class Request {

    private String action;
    private String data;
    private String name;

    public Request(String action) {
        this.action = action;
        this.data= "";
        this.name="";
    }

    public Request(String action, String data) {
        this.action = action;
        this.data = data;
        this.name="";
    }

    public Request(String action, String data, String name) {
        this.action = action;
        this.data = data;
        this.name = name;
    }



    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
