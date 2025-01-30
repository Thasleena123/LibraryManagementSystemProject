package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.dto.BookRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {
    private  final JdbcTemplate jdbcTemplate;
    public  BookRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    public boolean save(Book book) {
        String sql = "INSERT INTO Books (title, author, category, available_copies, is_rare, max_borrow_days) VALUES (?, ?, ?, ?, ?, ?)";
        int rows=jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getCategory(),
                book.getAvailableCopies(), book.isRare(), book.getMaxBorrowDays());
        return rows == 1;
    }
    public boolean updateAvailableCopies(String title, int newAvailableCopies){
        String sql="update Books set available_copies = ? where title = ?;";

        int rows=jdbcTemplate.update(sql,newAvailableCopies, title);
        return rows == 1;
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM Books";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }
    public Book bookExistsByTitle(String title) {
        String sql = "SELECT * FROM Books WHERE title = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BookRowMapper() , title);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
