package com.example.e_commerce.Model;

public class AdminOrder {

    private String date, shipment_name, shipment_phone, shipment_address, shipment_city, state, time;

    public AdminOrder() {
    }

    public AdminOrder(String date, String shipment_name, String shipment_phone, String shipment_address, String shipment_city, String state, String time) {
        this.date = date;
        this.shipment_name = shipment_name;
        this.shipment_phone = shipment_phone;
        this.shipment_address = shipment_address;
        this.shipment_city = shipment_city;
        this.state = state;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShipment_name() {
        return shipment_name;
    }

    public void setShipment_name(String shipment_name) {
        this.shipment_name = shipment_name;
    }

    public String getShipment_phone() {
        return shipment_phone;
    }

    public void setShipment_phone(String shipment_phone) {
        this.shipment_phone = shipment_phone;
    }

    public String getShipment_address() {
        return shipment_address;
    }

    public void setShipment_address(String shipment_address) {
        this.shipment_address = shipment_address;
    }

    public String getShipment_city() {
        return shipment_city;
    }

    public void setShipment_city(String shipment_city) {
        this.shipment_city = shipment_city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

