package com.target.myRetail.model;

public final class ProductWithProductPrice {

    private String id;
    private String name;

    private Current_price current_price;

    public ProductWithProductPrice(String id, String name, Current_price current_price){
        this.id = id;
        this.name = name;
        this.current_price = current_price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public Current_price getCurrent_price() {
        return current_price;
    }

}

