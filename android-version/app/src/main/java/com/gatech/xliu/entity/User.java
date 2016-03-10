package com.gatech.xliu.entity;

/**
 * Created by lyleliu on 3/9/16.
 */
public class User {
    public String username,password;
    int id;

    public User(int id,String username,String password) {
        this.id=id;
        this.username=username;
        this.password=password;
    }

    public User(String username,String password) {
        this.username=username;
        this.password=password;
    }
}
