package com.example.simplebank.models;

import java.util.*;
import java.math.*;

abstract class transaction {
    protected int txnId;
    protected int accountId;

    public void deposit(int accountId, BigDecimal amount){
        
    }
}

class deposit extends transaction{

}

class withdraw extends transaction{

}
