package com.example.simplebank.models;

import java.math.BigDecimal;

public abstract class Account{
    protected int accountId;
    protected BigDecimal balance;

    Account(int accountId, BigDecimal balance){
        this.accountId = accountId;
        this.balance = balance;
    }


    public int getAccountId(){
        return this.accountId;
    }

    public void setAccountId(int accountId){
        this.accountId = accountId;
    }

    public BigDecimal getBalance(){
        return this.balance;
    }

    public boolean deposit(BigDecimal amount){
        if(amount.compareTo(new BigDecimal(0)) <= 0){
            return false;
        }
        this.balance.add(amount);
        return true;
    }

    public boolean withdraw(BigDecimal amount){
        if(amount.compareTo(this.balance) < 0){
            return false;
        }
        this.balance.subtract(amount);
        return true;
    }
    
    
}

public class CheckingAccount extends Account{
    static String type = "CHECKING";

    
    public CheckingAccount(int accountId, BigDecimal balance){
        super(accountId, balance);
    }

    public String getType(){
        return type;
    }

}

public class SavingAccount extends Account{
    static String type = "SAVINGS";

    public SavingAccount(int accountId, BigDecimal balance){
        super(accountId, balance);
    }

    public String getType(){
        return type;
    }
}
