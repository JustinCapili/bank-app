package com.example.simplebank.repos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    public List<Account> getAccountsByUserId(int userId) {
        if (mongoTemplate == null) {
            return List.of();
        }

        Query query = Query.query(Criteria.where("userId").is(userId));
        List<Document> documents = mongoTemplate.find(query, Document.class, "accounts");
        List<Account> accounts = new ArrayList<>();
        for (Document document : documents) {
            accounts.add(mapAccount(document));
        }
        return accounts;
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

    public Account updateAccount(Account account){
        if (account == null) {
            return null;
        }

        if (mongoTemplate == null) {
            return account;
        }

        Query query = Query.query(Criteria.where("_id").is(account.getAccountId()));
        Update update = new Update()
            .set("balance", new Decimal128(account.getBalance()))
            .set("transactions", account.getTransactions());

        mongoTemplate.updateFirst(query, update, "accounts");
        return account;
    }

    public User getUserById(int userId){
        if (mongoTemplate != null) {
            Query query = Query.query(Criteria.where("_id").is(userId));
            return mongoTemplate.exists(query, "users")
                    ? new User(userId, null, null)
                    : null;
        }

        List<User> users = AllData.users;
        for (User user : users){
            if (user.getUserId() == userId){
                return user;
            }
        }
        return null;
    }

    public Account createAccount(int userId, String accountType){
        if (mongoTemplate != null) {
            return createMongoAccount(userId, accountType);
        }

        User user = this.getUserById(userId);
        if (user == null || accountType == null){
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

    private Account createMongoAccount(int userId, String accountType) {
        if (accountType == null || getUserById(userId) == null) {
            return null;
        }

        String type = accountType.toUpperCase();
        if (!type.equals("SAVINGS") && !type.equals("CHECKING")) {
            return null;
        }

        Query userAccounts = Query.query(Criteria.where("userId").is(userId));
        long accountCount = mongoTemplate.count(userAccounts, "accounts");
        int accountId = Integer.parseInt(String.valueOf(userId) + (accountCount + 1));

        Document account = new Document("_id", accountId)
                .append("userId", userId)
                .append("type", type)
                .append("balance", new Decimal128(BigDecimal.ZERO))
                .append("transactions", List.of());

        mongoTemplate.insert(account, "accounts");
        return mapAccount(account);
    }
}