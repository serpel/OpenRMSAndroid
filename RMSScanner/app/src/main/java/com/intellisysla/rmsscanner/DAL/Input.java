package com.intellisysla.rmsscanner.DAL;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by alienware on 12/12/2016.
 */

public class Input {
    private String name;
    private int quantity;
    private String location;
    Timestamp date;

    public Input(String name, int quantity, String location, Timestamp date) {
        this.name = name;
        this.quantity = quantity;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
