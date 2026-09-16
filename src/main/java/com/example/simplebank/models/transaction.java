package com.example.simplebank.models;

import java.util.*;
import java.math.BigDecimal;

public abstract class Transaction {
    protected int txnId;
    protected int accountId;

    Transaction(int accountId, int txnId){
        this.accountId = accountId;
        this.txnId = txnId;
    }

    public int getTxnId(){
        return txnId;
    }

    public int getAccountId(){
        return accountId;
    }

        
}
