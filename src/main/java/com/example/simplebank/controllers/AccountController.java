package com.example.simplebank.controllers;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<Account> getAccountById(@PathVariable int id){
        Account account = accountService.getAccount(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    } 

    @GetMapping("/users/{userId}/accounts")
    public List<Account> getAccountsByUserId(@PathVariable int userId){
        return accountService.getAccountsByUserId(userId);
    }

    @PostMapping("/accounts")
    public Account createAccount(@RequestBody AccountCreationRequest request){
        return accountService.createAccount(request.userId(), request.accountType());
    }

    @PutMapping("/accounts/{id}/deposit")
    public AmountResponse depositMoney(@PathVariable int id, @RequestBody AmountRequest request){
        accountService.deposit(id, request.amount());
        return new AmountResponse(request.amount());
    }

    @PutMapping("/accounts/{id}/withdraw")
    public AmountResponse withdrawMoney(@PathVariable int id, @RequestBody AmountRequest request){
        accountService.withdraw(id, request.amount());
        return new AmountResponse(request.amount());
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable int id){
        return accountService.getTransactions(id);
    }

    public record AccountCreationRequest(int userId, String accountType) {
    }

    public record AmountResponse(BigDecimal amount) {
    }

    public record AmountRequest(BigDecimal amount){
    }
}
