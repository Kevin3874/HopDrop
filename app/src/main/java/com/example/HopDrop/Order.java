package com.example.HopDrop;

public class Order {
    private String customer_name;
    private String src;
    private String dest;
    private float fee;

    public Order(String customer_name, String src, String dest, float fee) {
        this.customer_name = customer_name;
        this.src = src;
        this.dest = dest;
        this.fee = fee;
    }

    public String getCustomer_name() { return customer_name; }
    public String getSrc() { return src; }
    public String getDest() { return dest; }
    public Float getFee() { return fee; }
}

