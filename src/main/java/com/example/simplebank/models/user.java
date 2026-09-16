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

    public int getNewAccNum(){
        String idString = String.valueOf(this.userId) + String.valueOf(accounts.size() + 1);
        return Integer.parseInt(idString);
    }

}
