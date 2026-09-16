package com.example.simplebank.models;

public class Withdraw extends Transaction{
    static String type = "WITHDRAW";

    Withdraw(int accountId, int txnId){
        super(accountId, txnId);
    }
}
