package com.example.myapplication.objects;

public class cookObject {
    private int id;
    private String foodItem;
    private String tables;
    public cookObject(){}
    public cookObject(int _id,String foodsel,String table){
        id=_id;
        foodItem=foodsel;
        tables=table;
    }

    public int getId() {
        return id;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public String getTables() {
        return tables;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }
}
