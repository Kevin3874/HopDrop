package com.example.HopDrop;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Order implements Serializable {
    private final String customer_name;
    private String deliverer_name;
    private final String fromLocation;
    private final String dest;
    private final String fee;
    private final String notes;
    private int state;
    private String orderID;
    Bitmap image;

    public Order() {
        this.customer_name = "default";
        this.fromLocation = "default";
        this.dest = "default";
        this.fee = "3";
        this.notes = "default";
        this.state = -1;
        this.deliverer_name = "";
    }

    public Order(String customer_name, String fromLocation, String dest, String fee, String notes, Bitmap image) {
        this.customer_name = customer_name;
        this.fromLocation = fromLocation;
        this.dest = dest;
        this.fee = fee;
        this.notes = notes;
        state = -1;
        this.deliverer_name = "Pending Deliverer";
        orderID = "test";
        this.image = image;

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

    @PropertyName("deliverer_name")
    public String getDeliverer() { return deliverer_name; }
    @PropertyName("orderID")
    public String getOrderID() { return orderID; }

    public Bitmap getImage() {
        return image;
    }

    public void setOrderID(String id) {
        this.orderID = id;
    }

    public void setDeliverer(String name) {
        this.deliverer_name = name;
    }
    public int getState() {return state;}

    public void setState(int new_state) {
        state = new_state;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

