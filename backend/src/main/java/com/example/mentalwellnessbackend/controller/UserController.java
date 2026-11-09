
package com.example.mentalwellnessbackend.controller;

import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam String name,
            @RequestParam String password) {

        try {
            User user = userService.registerUser(name, password);

            return ResponseEntity.ok("User registered successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestParam String name,
            @RequestParam String password,
            HttpSession session) {

        User user = userService.authenticateUser(name, password);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body("Invalid name or password");
        }

        // Store logged-in user information in session
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getName());

        System.out.println("LOGIN SUCCESS");
        System.out.println("SESSION ID: " + session.getId());
        System.out.println("USER ID: " + session.getAttribute("userId"));

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok("Logged out successfully");
    }
}




