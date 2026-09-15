package com.example.simplebank.repos;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.simplebank.models.Account;

import utilities.AllData;

@Repository
public class AccountRepository {

    public Account getAccountById(int id) {
        List<Account> accounts = AllData.accs;
        for (Account acc : accounts) {
            if (acc.getAccountId() == id) {
                return acc;
            }
        }
        return null;
    }
}