package com.example.simplebank.services;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.simplebank.models.Account;
import com.example.simplebank.repos.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account getAccount(int id){
        return accountRepository.getAccountById(id);
    }

    public Account createAccount(int userId, String accountType){
        return null;
    }

    public boolean deposit(int accountId, BigDecimal amount){
        Account account = accountRepository.getAccountById(accountId);
        return account.deposit(amount);
    }

    public boolean withdraw(int accountId, BigDecimal amount){
        Account account = accountRepository.getAccountById(accountId);
        return account.withdraw(amount);
    }

    public void getTransactions(int accountId){}
}
