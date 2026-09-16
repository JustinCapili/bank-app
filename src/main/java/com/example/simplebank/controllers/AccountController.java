package com.example.simplebank.controllers;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplebank.models.*;
import com.example.simplebank.services.AccountService;


@RestController
@RequestMapping("/api")
public class AccountController {

    private AccountService accountService;

    @Autowired 
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable int id){
        Account account = accountService.getAccount(id);
        return account;
    } 

    @GetMapping("/accounts/{id}/deposit")
    public void depositMoney(@PathVariable int id){
        accountService.deposit(id, new BigDecimal(500));
    }

    @GetMapping("/accounts/{id}/withdraw")
    public void withdrawMoney(@PathVariable int id){
        accountService.withdraw(id, new BigDecimal(200));
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactions(int id){
        return accountService.getTransactions(id);
    }
}
