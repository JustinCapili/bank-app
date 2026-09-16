package com.example.simplebank.models;

import java.math.BigDecimal;
import java.util.*;


public abstract class Account{
    protected int accountId;
    protected BigDecimal balance;
    protected List<Transaction> transactions;

    Account(int accountId, BigDecimal balance){
        this.accountId = accountId;
        this.balance = balance;
        this.transactions = new ArrayList<>();
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
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            return false;
        }
        this.balance = this.balance.add(amount);

        String idString = String.valueOf(this.accountId) + String.valueOf(transactions.size());
        int newTransactionId = Integer.parseInt(idString);
        transactions.add(new Deposit(this.accountId, newTransactionId));

        return true;
    }

    public boolean withdraw(BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(this.balance) > 0){
            return false;
        }
        this.balance = this.balance.subtract(amount);

        String idString = String.valueOf(this.accountId) + String.valueOf(transactions.size());
        int newTransactionId = Integer.parseInt(idString);
        transactions.add(new Withdraw(this.accountId, newTransactionId));
    
        return true;
    }

    public List<Transaction> getTransactions(){
        return this.transactions;
    }
    
    
}

