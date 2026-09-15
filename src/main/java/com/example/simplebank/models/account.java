package com.example.simplebank.models;

public abstract class Account{
    protected int accountId;
    protected double balance;

    Account(int accountId, double balance){
        this.accountId = accountId;
        this.balance = balance;
    }


    public int getAccountId(){
        return this.accountId;
    }

    public double getBalance(){
        return this.balance;
    }
    
}

public class CheckingAccount extends Account{
    static String type = "CHECKING";

    
    public CheckingAccount(int accountId, double balance){
        super(accountId, balance);
    }

    public String getType(){
        return type;
    }

}

public class SavingAccount extends Account{
    static String type = "SAVINGS";

    public SavingAccount(int accountId, double balance){
        super(accountId, balance);
    }

    public String getType(){
        return type;
    }
}
