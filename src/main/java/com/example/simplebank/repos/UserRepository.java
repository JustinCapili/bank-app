package com.example.simplebank.repos;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.simplebank.models.User;

import utilities.AllData;

@Repository
public class UserRepository {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;
    private int numofUsers;

    @Autowired
    public UserRepository(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
        this.numofUsers = (int)mongoTemplate.estimatedCount("users");
    }

    public UserRepository() {
        this.mongoTemplate = null;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.numofUsers = 0;
    }

    public long getNumOfUsers(){
        return this.numofUsers;
    }

    public User getUserById(int userId) {
        if (mongoTemplate != null) {
            Query query = Query.query(Criteria.where("_id").is(userId));
            Document document = mongoTemplate.findOne(query, Document.class, "users");
            return document == null ? null : mapUser(document);
        }

        for (User user : AllData.users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public User createUser(String name, String email, String password) {
        if (name == null || email == null || password == null || password.isBlank()) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(password);

        if (mongoTemplate != null) {
            int userId = getNextUserId();

            Document user = new Document("_id", userId)
                    .append("name", name)
                    .append("email", email)
                    .append("password", hashedPassword);

            mongoTemplate.insert(user, "users");
            this.numofUsers += 1;
            return mapUser(user);
        }

        int userId = this.numofUsers + 1;
        User existingUser = getUserById(userId);
        if (existingUser != null) {
            return null;
        }

        User newUser = new User(userId, name, email, hashedPassword);
        AllData.users.add(newUser);
        return newUser;
    }

    public boolean verifyPassword(int userId, String rawPassword) {
        User user = getUserById(userId);
        return user != null && user.getPassword() != null
                && rawPassword != null
                && passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // relies on the current max _id rather than a cached count, which can go stale/inaccurate
    private int getNextUserId() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "_id")).limit(1);
        Document lastUser = mongoTemplate.findOne(query, Document.class, "users");
        int maxId = lastUser == null ? 0 : lastUser.getInteger("_id");
        return maxId + 1;
    }

    private User mapUser(Document document) {
        int userId = document.getInteger("_id");
        String name = document.getString("name");
        String email = document.getString("email");
        String password = document.getString("password");
        return new User(userId, name, email, password);
    }
}
