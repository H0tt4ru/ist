package com.example.shared_utils.utils;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidation {

    public String validateUser(String username, String password) {
        if (username == null || username.isEmpty()) {
            return "Username is required";
        }
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        return null;
    }
}
