package com.example.LibraryManagementSystem.services;

import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public  void  userCreation(User user){
        String   encryptedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.userCreation(user);
    }
    public Optional<User> getUserById(int userId){
        return  userRepository.getUserById(userId);
    }
    public void updateUser(User user){
        String   encryptedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.updateUser(user);
    }
    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}

