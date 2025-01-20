package com.example.LibraryManagementSystem.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class EncodingServices {
    private final String salt = BCrypt.gensalt();

    public String encodePassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean checkPassword(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }
}
