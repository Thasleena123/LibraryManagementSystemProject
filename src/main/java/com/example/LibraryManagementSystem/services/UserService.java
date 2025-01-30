package com.example.LibraryManagementSystem.services;

import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.enums.Role;
import com.example.LibraryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    EncodingServices encodingServices;

    @Autowired
    public UserService(UserRepository userRepository, EncodingServices encodingServices) {
        this.userRepository = userRepository;
        this.encodingServices = encodingServices;
    }

    public void userCreation(User user) {
        String encryptedPassword = encodingServices.encodePassword(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.userCreation(user);
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    public void updateUser(User user) {
        String encryptedPassword = encodingServices.encodePassword(user.getPassword());
        user.setPassword(user.getPassword());
        userRepository.updateUser(user);
    }

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }



    public boolean authenticateuser(String email, String password) {
        User user = userRepository.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("Authentication failes:users not found");
        }

        if (!encodingServices.checkPassword(password, user.getPassword())) {
            throw new RuntimeException("authentication failed due to incorrect password");
        }

        return true;
    }

}

