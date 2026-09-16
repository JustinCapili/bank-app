package com.example.simplebank.repos;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.simplebank.models.*;

import utilities.AllData;

@Repository
public class AccountRepository {

    public Account getAccountById(int id) {
        List<Account> accounts = AllData.accounts;
        for (Account acc : accounts) {
            if (acc.getAccountId() == id) {
                return acc;
            }
        }
        return null;
    }

    public User getUserById(int userId){
        List<User> users = AllData.users;
        for (User user : users){
            if (user.getUserId() == userId){
                return user;
            }
        }
        return null;
    }

    public Account createAccount(int userId, String accountType){
        User user = this.getUserById(userId);
        if (user == null){
            return null;
        }
        if (accountType.equalsIgnoreCase("SAVINGS")){
            SavingAccount acc = new SavingAccount(user.getNewAccNum(),new BigDecimal(0.0));
            user.addAccount(acc);
            AllData.accounts.add(acc);
            return acc;
        } 
        
        else if (accountType.equalsIgnoreCase("CHECKING")){
            CheckingAccount acc = new CheckingAccount(user.getNewAccNum(),new BigDecimal(0.0));
            user.addAccount(acc);
            AllData.accounts.add(acc);
            return acc;
        }
        
        return null;
    }
}