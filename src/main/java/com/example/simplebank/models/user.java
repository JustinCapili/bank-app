package com.example.simplebank.models;

import com.example.simplebank.models.Account;

class User {
    protected int userId;
    private String name;
    private String email;
    protected Account[] accounts;

    User(int userId, String name, String email){
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

}
