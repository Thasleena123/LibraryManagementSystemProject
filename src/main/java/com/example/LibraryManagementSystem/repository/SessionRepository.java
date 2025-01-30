package com.example.LibraryManagementSystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addSession(int userId,int sessionId){
        String sql=" INSERT INTO session (user_id, session_id) VALUES (?, ?)";
        int rows=jdbcTemplate.update(sql,userId,sessionId);
     return rows>0 ;
    }
    
}
