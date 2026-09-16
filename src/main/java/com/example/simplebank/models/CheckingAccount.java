package com.example.simplebank.models;

import java.math.BigDecimal;

public class CheckingAccount extends Account{
    static String type = "CHECKING";

    
    public CheckingAccount(int accountId, BigDecimal balance){
        super(accountId, balance);
    }

    public String getType(){
        return type;
    }

}