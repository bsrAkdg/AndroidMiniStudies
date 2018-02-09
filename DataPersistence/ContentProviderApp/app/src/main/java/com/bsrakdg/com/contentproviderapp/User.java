package com.bsrakdg.com.contentproviderapp;

/**
 * Created by bakdag on 9.02.2018.
 */

public class User{

    String name, phone;

    User(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}