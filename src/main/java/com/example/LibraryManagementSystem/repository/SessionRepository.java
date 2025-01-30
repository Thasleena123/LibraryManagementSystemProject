package com.example.LibraryManagementSystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public SessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public String getSessionId(int userId) {
        String sql = "SELECT session_id FROM sessions WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public boolean addOrUpdateSession(int userId, String sessionId) {
        String existingSessionId = getSessionId(userId);

        if (existingSessionId == null) {
            String insertSql = "INSERT INTO sessions (user_id, session_id) VALUES (?, ?)";
            int rows = jdbcTemplate.update(insertSql, userId, sessionId);
            return rows > 0;
        } else {
            String updateSql = "UPDATE sessions SET session_id = ? WHERE user_id = ?";
            int rows = jdbcTemplate.update(updateSql, sessionId, userId);
            return rows > 0;
        }
    }

}
