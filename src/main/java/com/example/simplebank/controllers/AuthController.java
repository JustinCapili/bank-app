package com.example.simplebank.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplebank.models.User;
import com.example.simplebank.security.JwtService;
import com.example.simplebank.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserController.AuthResponse> login(@RequestBody LoginRequest request){
        User user = userService.authenticate(request.userId(), request.password());
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtService.generateToken(user.getUserId());
        return ResponseEntity.ok(new UserController.AuthResponse(user, token));
    }

    public record LoginRequest(int userId, String password) {
    }
}
