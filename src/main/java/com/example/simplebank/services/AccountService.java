package com.example.simplebank.services;

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

    public Account getAccountById(int id){
        return accountRepository.getAccountById(id);
    }

}
