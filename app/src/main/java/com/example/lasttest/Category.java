package com.example.lasttest;

public class Category {
    public String name;
    public String address;
    public String number;
    public String image;

    public Category(){

    }

    public Category(String name, String address, String number, String image){
        this.name = name;
        this.address = address;
        this.number = number;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}