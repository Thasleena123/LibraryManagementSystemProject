package com.example.LibraryManagementSystem.services;

import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.repository.SessionRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    EncodingServices encodingServices;
    private final SessionRepository sessionRepository;

    @Autowired
    public UserService(UserRepository userRepository, EncodingServices encodingServices, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.encodingServices = encodingServices;
        this.sessionRepository = sessionRepository;
    }

    public void userCreation(User user) {
        String encryptedPassword = encodingServices.encodePassword(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.userCreation(user);
    }
    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    public boolean authenticateuser(String email, String password, HttpSession session) {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("Authentication failes:users not found");
        }

        if (!encodingServices.checkPassword(password, user.getPassword())) {
            throw new RuntimeException("authentication failed due to incorrect password");
        }

        sessionRepository.addOrUpdateSession(user.getUserId(), session.getId());

        return true;
    }



}

