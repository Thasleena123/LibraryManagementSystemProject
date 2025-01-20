package com.example.LibraryManagementSystem.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    public  User mapRow(ResultSet rs, int rowNum) throws SQLException{
        return  new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("membership_type")
        );
    }

}
