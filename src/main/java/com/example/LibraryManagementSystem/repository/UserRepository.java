package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.dto.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private  final UserRowMapper userRowMapper;

    public UserRepository(JdbcTemplate jdbcTemplate,UserRowMapper userRowMapper) {
        this.jdbcTemplate=jdbcTemplate;
        this.userRowMapper=userRowMapper;
    }
    public  void userCreation(User user) {
        String sql = "insert into User(email, password, role, first_name, last_name, membership_type) values(?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole(), user.getFirstName(), user.getLastName(), user.getMembership());
    }
   public Optional<User> getUserByEmail(String email){
        String sql="select * from users where email=?";
       List<User> users=jdbcTemplate.query(sql,userRowMapper,email);
       return  users.isEmpty()? Optional.empty():Optional.of(users.get(0));
   }
    public Optional<User> getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, userId);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public  void updateUser(User user) {
        String sql = "update users set email=?,password=?,role=?,first_name=?,last_name=?,membership=?;";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole(), user.getFirstName(), user.getLastName(), user.getLastName());
    }
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
    }

