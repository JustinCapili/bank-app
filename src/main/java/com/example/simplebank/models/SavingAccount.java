package com.example.simplebank.models;

import java.math.BigDecimal;

public class SavingAccount extends Account{
    static String type = "SAVINGS";

    public SavingAccount(int accountId, BigDecimal balance){
        super(accountId, balance);
    }

    public String getType(){
        return type;
    }
}