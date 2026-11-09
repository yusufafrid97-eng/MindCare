
package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE ACCOUNT
    public User registerUser(String name, String password) {

        if (userRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                    "Username already exists. Please login."
            );
        }

        User user = new User();
        user.setName(name);
        user.setPasswordHash(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    // AUTHENTICATE USER
    public User authenticateUser(String name, String password) {

        List<User> users = userRepository.findAllByName(name);

        for (User user : users) {

            if (passwordEncoder.matches(
                    password,
                    user.getPasswordHash())) {

                return user;
            }
        }

        return null;
    }

    // LOGIN CHECK
    public boolean loginUser(String name, String password) {
        return authenticateUser(name, password) != null;
    }
}


