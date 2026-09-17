package com.example.simplebank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplebank.models.User;
import com.example.simplebank.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request){
        User user = userService.createUser(request.name(), request.email());
        return user == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        User user = userService.getUser(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    public record UserCreationRequest(int userId, String name, String email) {
    }
}
