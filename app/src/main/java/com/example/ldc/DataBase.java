package com.example.ldc;

public class DataBase {
    private String Data;
    String id;

    public DataBase() {
    }

    public DataBase(String data, String id) {
        Data = data;
        this.id = id;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
