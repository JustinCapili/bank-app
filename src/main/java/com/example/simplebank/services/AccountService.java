package com.example.simplebank.services;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.simplebank.models.*;
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

    public List<Account> getAccountsByUserId(int userId){
        return accountRepository.getAccountsByUserId(userId);
    }

    public Account createAccount(int userId, String accountType){
        return accountRepository.createAccount(userId, accountType);
    }

    public boolean deposit(int accountId, BigDecimal amount){
        Account account = accountRepository.getAccountById(accountId);
        if (account == null || !account.deposit(amount)) {
            return false;
        }
        accountRepository.updateAccount(account);
        return true;
    }

    public boolean withdraw(int accountId, BigDecimal amount){
        Account account = accountRepository.getAccountById(accountId);
        if (account == null || !account.withdraw(amount)) {
            return false;
        }
        accountRepository.updateAccount(account);
        return true;
    }

    public  List<Transaction> getTransactions(int accountId){
        Account account = accountRepository.getAccountById(accountId);
        return account.getTransactions();
    }
}
