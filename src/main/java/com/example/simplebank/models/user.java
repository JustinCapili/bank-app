package com.example.simplebank.models;

import java.math.BigDecimal;
import java.util.*;
import com.example.simplebank.models.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    protected int userId;
    private String name;
    private String email;
    private String password;
    protected List<Account> accounts;

    public User(int userId, String name, String email){
        this(userId, name, email, null);
    }

    public User(int userId, String name, String email, String password){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public void createAccount(String accountType){

        if(accountType == "CHECKING"){
            accounts.add(new CheckingAccount(this.getNewAccNum(), new BigDecimal(0.0)));
        }else{
            
            accounts.add(new SavingAccount(this.getNewAccNum(), new BigDecimal(0.0)));
        }
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public int getUserId(){
        return this.userId;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }

    public int getNewAccNum(){
        String idString = String.valueOf(this.userId) + String.valueOf(accounts.size() + 1);
        return Integer.parseInt(idString);
    }

}
