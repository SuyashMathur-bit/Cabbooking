package com.example.cabbooking;

public class Booking {
    private String pickup;
    private String drop;
    private String price;
    private String date;

    public Booking(String pickup, String drop, String price, String date) {
        this.pickup = pickup;
        this.drop = drop;
        this.price = price;
        this.date = date;
    }

    // Getters
    public String getPickup() { return pickup; }
    public String getDrop() { return drop; }
    public String getPrice() { return price; }
    public String getDate() { return date; }

    // Setters (optional)
    public void setPickup(String pickup) { this.pickup = pickup; }
    public void setDrop(String drop) { this.drop = drop; }
    public void setPrice(String price) { this.price = price; }
    public void setDate(String date) { this.date = date; }
}
