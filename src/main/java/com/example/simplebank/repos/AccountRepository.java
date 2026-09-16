package com.example.simplebank.repos;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.simplebank.models.*;

import utilities.AllData;

import org.springframework.data.mongodb.core.MongoTemplate;

@Repository
public class AccountRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccountRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public AccountRepository() {
        this.mongoTemplate = null;
    }

    public Account getAccountById(int id) {
        if (mongoTemplate != null) {
            Document document = mongoTemplate.findById(id, Document.class, "accounts");
            return document == null ? null : mapAccount(document);
        }

        // List<Account> accounts = AllData.accounts;
        // for (Account acc : accounts) {
        //     if (acc.getAccountId() == id) {
        //         return acc;
        //     }
        // }
        return null;
    }

    private Account mapAccount(Document document) {
        int accountId = document.getInteger("_id");
        BigDecimal balance = readBalance(document.get("balance"));
        String type = document.getString("type");

        if ("CHECKING".equalsIgnoreCase(type)) {
            return new CheckingAccount(accountId, balance);
        }
        return new SavingAccount(accountId, balance);
    }

    private BigDecimal readBalance(Object value) {
        if (value instanceof Decimal128 decimal128) {
            return decimal128.bigDecimalValue();
        }
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
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