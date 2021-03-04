package com.target.myRetail.model;

public class Current_price {
    double value;
    String currency_code;

    public Current_price(double value, String currency_code){
        this.value = value;
        this.currency_code = currency_code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }


}
