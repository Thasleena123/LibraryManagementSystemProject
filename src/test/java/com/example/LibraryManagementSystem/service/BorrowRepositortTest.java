package com.example.LibraryManagementSystem.service;
import com.example.LibraryManagementSystem.repository.BorrowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
public class BorrowRepositortTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BorrowRepository borrowRepository;

    private final int userId = 1;
    private final Long bookId = 101L;
    private final Date returnDate = new Date(System.currentTimeMillis() + (7L * 24L * 60L * 60L * 1000L));

    @BeforeEach
    void setUp() {
        String createBooksTable = "CREATE TABLE Books (" +
                "book_id BIGINT PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "author VARCHAR(255), " +
                "category VARCHAR(255), " +
                "available_copies INT, " +
                "is_rare BOOLEAN, " +
                "max_borrow_days INT)";
        String createBorrowingTable = "CREATE TABLE Borrowing (" +
                "borrow_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT, " +
                "book_id BIGINT, " +
                "borrow_date DATE, " +
                "return_date DATE, " +
                "fine DOUBLE, " +
                "status VARCHAR(20))";

        jdbcTemplate.execute(createBooksTable);
        jdbcTemplate.execute(createBorrowingTable);

        String insertBook = "INSERT INTO Books (book_id, title, author, category, available_copies, is_rare, max_borrow_days) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertBook, bookId, "Java Programming", "John Doe", "Programming", 10, false, 14);

        String insertBorrowing = "INSERT INTO Borrowing (user_id, book_id, borrow_date, return_date, fine, status) " +
                "VALUES (?, ?, CURRENT_DATE, ?, 0.0, 'BORROWED')";
        jdbcTemplate.update(insertBorrowing, userId, bookId, returnDate);
    }

    @Test
    void testGetBorrowedBooksCount_Success() {
        // Act
        int borrowedBooksCount = borrowRepository.getBorrowedBooksCount(userId);

        // Assert
        assertEquals(1, borrowedBooksCount);
    }

    @Test
    void testGetAvailableCopies_Success() {
        // Act
        int availableCopies = borrowRepository.getAvailableCopies(bookId);

        // Assert
        assertEquals(10, availableCopies);
    }

    @Test
    void testInsertBorrowRecord_Success() {
        // Act
        borrowRepository.insertBorrowRecord(userId, bookId, returnDate);

        // Assert
        String query = "SELECT COUNT(*) FROM Borrowing WHERE user_id = ? AND book_id = ? AND status = 'BORROWED'";
        int count = jdbcTemplate.queryForObject(query, new Object[]{userId, bookId}, Integer.class);
        assertEquals(2, count);
    }

    @Test
    void testUpdateBookAvailability_Success() {
        // Act
        borrowRepository.updateBookAvailability(bookId, -1); // Decrease availability by 1

        // Assert
        int availableCopies = borrowRepository.getAvailableCopies(bookId);
        assertEquals(9, availableCopies);
    }
    @Test
    void testCalculateFine_NoFine() {

        double fine = borrowRepository.calculateFine(1L, new Date());

        assertEquals(0.0, fine);
    }

    @Test
    void testCalculateFine_WithFine() {
        Date lateReturnDate = new Date(System.currentTimeMillis() - (10L * 24L * 60L * 60L * 1000L));
        String updateReturnDate = "UPDATE Borrowing SET return_date = ? WHERE borrow_id = 1";
        jdbcTemplate.update(updateReturnDate, lateReturnDate);

        double fine = borrowRepository.calculateFine(1L, new Date());
        assertEquals(10.0, fine);
    }

    @Test
    void testDoesBorrowRecordExist_True() {
        boolean exists = borrowRepository.doesBorrowRecordExist(1L);

        assertTrue(exists);
    }

    @Test
    void testDoesBorrowRecordExist_False() {
        boolean exists = borrowRepository.doesBorrowRecordExist(999L);
        assertFalse(exists);
    }

    @Test
    void testGetBorrowingHistory_Success() {

        List<Map<String, Object>> history = borrowRepository.getBorrowingHistory(userId);

        assertNotNull(history);
        assertTrue(history.size() > 0);
    }
    @Test
    void testGetBorrowDetails_Success() {
        // Act
        Map<String, Object> borrowDetails = borrowRepository.getBorrowDetails(1L);

        assertNotNull(borrowDetails);
        assertEquals("BORROWED", borrowDetails.get("status"));
    }
}


