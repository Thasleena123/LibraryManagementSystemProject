package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.dto.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    public void userCreation(User user) {
        String sql = "insert into Users (email, password, role, first_name, last_name, membership_type) values(?,?,?,?,?,?);";
        int rowsAffect = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole().name(), user.getFirstName(), user.getLastName(), user.getMembership().name());
        System.out.println(rowsAffect);
        if (rowsAffect == 1) {
            System.out.println("Added USER");
        } else {
            System.out.println("Failed to add");
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, email);
            return user;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);

    }
}

