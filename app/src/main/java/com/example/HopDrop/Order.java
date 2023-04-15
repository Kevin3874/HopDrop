package com.example.HopDrop;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Order implements Serializable {
    private final String orderID;
    private final String customer_name;
    private final String fromLocation;
    private final String dest;
    private final String fee;

    private final String notes;

    private int state;

    public Order() {
        this.orderID = "jal";
        this.customer_name = "default";
        this.fromLocation = "default";
        this.dest = "default";
        this.fee = "3";
        this.notes = "default";
        this.state = 0;
    }

    public Order(String customer_name, String fromLocation, String dest, String fee, String notes, String orderID) {
        this.customer_name = customer_name;
        this.fromLocation = fromLocation;
        this.dest = dest;
        this.fee = fee;
        this.notes = notes;
        state = 0;
        this.orderID = orderID;

    }
    @PropertyName("customer_name")
    public String getCustomerName() {
        return customer_name;
    }

    @PropertyName("fromLocation")
    public String getFrom() {
        return fromLocation;
    }

    @PropertyName("dest")
    public String getDest() {
        return dest;
    }

    @PropertyName("fee")
    public String getFee() {
        return fee;
    }

    @PropertyName("notes")
    public String getNotes() {
        return notes;
    }

    @PropertyName("orderID")
    public String getId() {
        return orderID;
    }

    public int getState() {return state;}

    public void setState(int new_state) {
        state = new_state;
    }
    public void updateState() {
        state++;
    }

}

