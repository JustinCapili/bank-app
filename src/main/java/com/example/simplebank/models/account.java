package com.example.simplebank.models;

abstract class Account extends user{
    protected int accountId;
    protected double balance;

    
}

class CheckingAccount extends Account{
    public double fee;

}

class SavingAccount extends Account{
    public double interestRate;
}
