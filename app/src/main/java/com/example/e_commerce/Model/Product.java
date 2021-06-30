package com.example.e_commerce.Model;

public class Product
{
    private String name,description,price,pid,date,time,image,brand,category;

    public Product()
    {

    }

    public Product(String name, String description, String price, String pid, String date, String time, String image, String brand,String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.image = image;
        this.brand = brand;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getPid() {
        return pid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
