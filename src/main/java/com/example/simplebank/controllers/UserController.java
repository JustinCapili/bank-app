package com.example.simplebank.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplebank.models.User;
import com.example.simplebank.security.JwtService;
import com.example.simplebank.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/users")
    public ResponseEntity<AuthResponse> createUser(@RequestBody UserCreationRequest request){
        User user = userService.createUser(request.name(), request.email(), request.password());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        String token = jwtService.generateToken(user.getUserId());
        return ResponseEntity.ok(new AuthResponse(user, token));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id, Authentication authentication){
        if (!isSelf(authentication, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userService.getUser(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    static boolean isSelf(Authentication authentication, int userId) {
        return authentication != null && authentication.getPrincipal() instanceof Integer principalId
                && principalId == userId;
    }

    public record UserCreationRequest(String name, String email, String password) {
    }

    public record AuthResponse(User user, String token) {
    }
}
