package com.example.simplebank.models;

public class Account extends User{
    protected int accountId;
    protected double balance;

    public Account(){
        this.balance = 0;
    }
    
    public Account(int accountId){
        this();
        this.accountId = accountId;
    }

    public Account(int accountId, double balance){
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

class CheckingAccount extends Account{
    public double fee;

}

class SavingAccount extends Account{
    public double interestRate;
}
