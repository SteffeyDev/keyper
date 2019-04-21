package com.example.keypermobile.models;

import java.util.ArrayList;

public class User {
    String username;
    String email;
    String password;
    ArrayList<Site> sites;

    public User(String username, String email, String password, ArrayList<Site> sites) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.sites = sites;
    }

    public User() {
    }
}
