package com.example.LibraryManagementSystem.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BorrowRowMapper implements RowMapper<Borrow> {
    @Override
    public Borrow mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Borrow(
                rs.getInt("borrow_id"),
                rs.getInt("user_id"),
                rs.getInt("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                rs.getDouble("fine"),
                rs.getString("status")
        );

    }
}
