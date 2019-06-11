package com.example.lasttest;

public class Menu {
    private String menu_name;
    private String menu_price;
    private String menu_comment;

    public Menu() {
    }

    public Menu(String menu_name, String menu_price, String menu_comment) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_comment = menu_comment;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_comment() {
        return menu_comment;
    }

    public void setMenu_comment(String menu_comment) {
        this.menu_comment = menu_comment;
    }
}


