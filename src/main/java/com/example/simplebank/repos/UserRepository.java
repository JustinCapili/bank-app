package com.example.simplebank.repos;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.simplebank.models.User;

import utilities.AllData;

@Repository
public class UserRepository {

    private final MongoTemplate mongoTemplate;
    private int numofUsers;

    @Autowired
    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.numofUsers = (int)mongoTemplate.estimatedCount("users");
    }

    public UserRepository() {
        this.mongoTemplate = null;
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

    public User createUser(String name, String email) {
        int userId = this.numofUsers + 1;
        if (userId <= 0 || name == null || email == null) {
            return null;
        }

        if (mongoTemplate != null) {
            if (getUserById(userId) != null) {
                return null;
            }

            Document user = new Document("_id", userId)
                    .append("name", name)
                    .append("email", email);

            mongoTemplate.insert(user, "users");
            this.numofUsers += 1;
            return mapUser(user);
        }

        User existingUser = getUserById(userId);
        if (existingUser != null) {
            return null;
        }

        User newUser = new User(userId, name, email);
        AllData.users.add(newUser);
        return newUser;
    }

    private User mapUser(Document document) {
        int userId = document.getInteger("_id");
        String name = document.getString("name");
        String email = document.getString("email");
        return new User(userId, name, email);
    }
}
