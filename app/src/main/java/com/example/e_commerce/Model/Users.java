package com.example.e_commerce.Model;

public class Users
{
    private String name,phone, password,address,image,securityBirth,securitySport;

    public Users()
    {

    }

    public Users(String name, String phone, String password, String address, String image,String securityBirth,String securitySport) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.image = image;
        this.securityBirth=securityBirth;
        this.securitySport=securitySport;
    }

    public String getSecurityBirth() {
        return securityBirth;
    }

    public void setSecurityBirth(String securityBirth) {
        this.securityBirth = securityBirth;
    }

    public String getSecuritySport() {
        return securitySport;
    }

    public void setSecuritySport(String securitySport) {
        this.securitySport = securitySport;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
