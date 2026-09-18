package com.example.simplebank.controllers;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Account> getAccountById(@PathVariable int id, Authentication authentication){
        if (!ownsAccount(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Account account = accountService.getAccount(id);
        return account == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(account);
    } 

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable int userId, Authentication authentication){
        if (!UserController.isSelf(authentication, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequest request, Authentication authentication){
        if (!UserController.isSelf(authentication, request.userId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(accountService.createAccount(request.userId(), request.accountType()));
    }

    @PutMapping("/accounts/{id}/deposit")
    public ResponseEntity<AmountResponse> depositMoney(@PathVariable int id, @RequestBody AmountRequest request, Authentication authentication){
        if (!ownsAccount(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        accountService.deposit(id, request.amount());
        return ResponseEntity.ok(new AmountResponse(request.amount()));
    }

    @PutMapping("/accounts/{id}/withdraw")
    public ResponseEntity<AmountResponse> withdrawMoney(@PathVariable int id, @RequestBody AmountRequest request, Authentication authentication){
        if (!ownsAccount(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        accountService.withdraw(id, request.amount());
        return ResponseEntity.ok(new AmountResponse(request.amount()));
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable int id, Authentication authentication){
        if (!ownsAccount(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(accountService.getTransactions(id));
    }

    private boolean ownsAccount(Authentication authentication, int accountId) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Integer principalId)) {
            return false;
        }
        Integer ownerId = accountService.getOwnerUserId(accountId);
        return ownerId != null && ownerId.equals(principalId);
    }

    public record AccountCreationRequest(int userId, String accountType) {
    }

    public record AmountResponse(BigDecimal amount) {
    }

    public record AmountRequest(BigDecimal amount){
    }
}
