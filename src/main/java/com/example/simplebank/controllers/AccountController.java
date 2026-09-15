package com.example.simplebank.controllers;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplebank.models.Account;
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
}
