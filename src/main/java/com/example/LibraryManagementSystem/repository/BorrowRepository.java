package com.example.LibraryManagementSystem.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class BorrowRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Map<String, Object> getMembershipDetails(Long userId) {
        String userQuery = "SELECT membership_id FROM users WHERE user_id = ?";
        Long membershipId = jdbcTemplate.queryForObject(userQuery, new Object[]{userId}, Long.class);

        if (membershipId == null) {
            return null;
        }

        String membershipQuery = "SELECT borrow_limit, max_borrow_days FROM memberships WHERE membership_id = ?";
        return jdbcTemplate.queryForMap(membershipQuery, membershipId);
    }


    public int getBorrowedBooksCount(Long userId) {
        String query = "SELECT COUNT(*) FROM borrows WHERE user_id = ? AND status = 'BORROWED'";
        return jdbcTemplate.queryForObject(query, new Object[]{userId}, Integer.class);
    }


    public Map<String, Object> getBookDetails(Long bookId) {
        String query = "SELECT available_copies, is_rare FROM books WHERE book_id = ?";
        return jdbcTemplate.queryForMap(query, bookId);
    }
    public void insertBorrowRecord(Long userId, Long bookId, Date returnDate) {
        String query = "INSERT INTO borrows (user_id, book_id, borrow_date, return_date, fine, status) " +
                "VALUES (?, ?, CURRENT_DATE, ?, 0.0, 'BORROWED')";
        jdbcTemplate.update(query, userId, bookId, returnDate);
    }

    public int getAvailableCopies(Long bookId) {
        String query = "SELECT available_copies FROM books WHERE book_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{bookId}, Integer.class);
    }


    public void updateBookAvailability(Long bookId, int delta) {
        String query = "UPDATE books SET available_copies = available_copies + ? WHERE book_id = ?";
        jdbcTemplate.update(query, delta, bookId);
    }

    public Map<String, Object> getBorrowDetails(Long borrowId) {
        String query = "SELECT * FROM borrows WHERE borrow_id = ?";
        return jdbcTemplate.queryForMap(query, borrowId);
    }

    public void updateBorrowRecord(Long borrowId, double fine) {
        String query = "UPDATE borrows SET return_date = CURRENT_DATE, fine = ?, status = 'RETURNED' WHERE borrow_id = ?";
        jdbcTemplate.update(query, fine, borrowId);
    }
    public boolean doesBorrowRecordExist(Long borrowId) {
        String query = "SELECT COUNT(*) FROM borrows WHERE borrow_id = ? AND status = 'BORROWED'";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{borrowId}, Integer.class);
        return count != null && count > 0;
    }
    public double calculateFine(Long borrowId, Date currentDate) {
        String query = "SELECT return_date FROM borrows WHERE borrow_id = ?";
        Date returnDate = jdbcTemplate.queryForObject(query, new Object[]{borrowId}, Date.class);
        long diffInMillis = currentDate.getTime() - returnDate.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
        if (diffInDays > 0) {
            return diffInDays;
        }
        return 0;
    }
    public Long getBookIdFromBorrowRecord(Long borrowId) {
        String query = "SELECT book_id FROM borrows WHERE borrow_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{borrowId}, Long.class);
    }
    public List<Map<String, Object>> getBorrowingHistory(Long userId) {
        String query = "SELECT b.title, br.borrow_date, br.return_date, br.fine, br.status " +
                "FROM borrows br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.user_id = ? " +
                "ORDER BY br.borrow_date DESC";
        return jdbcTemplate.queryForList(query, userId);
    }
}

