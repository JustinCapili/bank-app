package com.example.simplebank.models;

abstract class transaction {
    protected int txnId;
    protected int accountId;
}

class deposit extends transaction{

}

class withdraw extends transaction{

}
