package com.example.e_commerce.Model;

public class Cart {

    private String pid,pname,price,quantity,discount,image;

    public Cart() {
    }

    public Cart(String pid, String pname, String price, String quantity, String discount,String PImage) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.image= image;
    }

    public String getimage() {
        return image;
    }

    public String getPid() {
        return pid;
    }

    public String getPname() {
        return pname;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setpImage(String pImage) {
        this.image = pImage;
    }
}
