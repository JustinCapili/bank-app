package com.example.simplebank.models;

import java.math.BigDecimal;
import java.util.*;
import com.example.simplebank.models.Account;

public class User {
    protected int userId;
    private String name;
    private String email;
    protected List<Account> accounts;

    public User(int userId, String name, String email){
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    public void createAccount(String accountType){

        if(accountType == "CHECKING"){
            String idString = String.valueOf(this.userId) + String.valueOf(accounts.size());
            int newAccountId = Integer.parseInt(idString);

            accounts.add(new CheckingAccount(newAccountId, new BigDecimal(0.0)));
        }else{

            String idString = String.valueOf(this.userId) + String.valueOf(accounts.size());
            int newAccountId = Integer.parseInt(idString);

            accounts.add(new SavingAccount(newAccountId, new BigDecimal(0.0)));
        }
    }

}
