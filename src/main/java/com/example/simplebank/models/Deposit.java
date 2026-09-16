package com.example.simplebank.models;

public class Deposit extends Transaction{
    static String type = "DEPOSIT";

    Deposit(int accountId, int txnId){
        super(accountId, txnId);
    }
}