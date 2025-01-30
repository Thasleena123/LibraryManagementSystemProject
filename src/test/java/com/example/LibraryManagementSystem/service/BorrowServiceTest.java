package com.example.LibraryManagementSystem.service;
import com.example.LibraryManagementSystem.repository.BorrowRepository;
import com.example.LibraryManagementSystem.services.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class BorrowServiceTest {


        @Mock
        private BorrowRepository borrowRepository;

        @InjectMocks
        private BorrowService borrowService;

        private final int userId = 1;
        private final Long bookId = 101L;
        private final int maxDays = 7;
        private final Long borrowId = 1001L;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testBorrowBook_Success() throws Exception {
            when(borrowRepository.getBorrowedBooksCount(userId)).thenReturn(3);
            when(borrowRepository.getAvailableCopies(bookId)).thenReturn(5);
            borrowService.borrowBook(userId, bookId, maxDays);
            verify(borrowRepository, times(1)).insertBorrowRecord(userId, bookId, any(Date.class));
            verify(borrowRepository, times(1)).updateBookAvailability(bookId, -1);
        }

        @Test
        void testBorrowBook_LimitExceeded() {
            when(borrowRepository.getBorrowedBooksCount(userId)).thenReturn(5);
            Exception exception = assertThrows(Exception.class, () -> {
                borrowService.borrowBook(userId, bookId, maxDays);
            });

            assertEquals("Borrow limit exceeded!", exception.getMessage());
        }

        @Test
        void testBorrowBook_BookUnavailable() {
            when(borrowRepository.getBorrowedBooksCount(userId)).thenReturn(3);
            when(borrowRepository.getAvailableCopies(bookId)).thenReturn(0);
            Exception exception = assertThrows(Exception.class, () -> {
                borrowService.borrowBook(userId, bookId, maxDays);
            });

            assertEquals("Book unavailable.", exception.getMessage());
        }

    @Test
    void testReturnBook_Success() {
        when(borrowRepository.doesBorrowRecordExist(borrowId)).thenReturn(true);
        when(borrowRepository.calculateFine(borrowId, new Date())).thenReturn(0.0);
        borrowService.returnBook(borrowId);
   verify(borrowRepository, times(1)).updateBorrowRecord(borrowId, 0.0);
        verify(borrowRepository, times(1)).updateBookAvailability(bookId, 1);
    }

    @Test
    void testReturnBook_RecordNotFound() {
        when(borrowRepository.doesBorrowRecordExist(borrowId)).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            borrowService.returnBook(borrowId);
        });
        assertEquals("Borrow record not found.", exception.getMessage());
    }

    @Test
    void testGetBorrowHistory() {
        List<Map<String, Object>> borrowHistory = new ArrayList<>();
        Map<String, Object> borrowRecord = new HashMap<>();
        borrowRecord.put("bookId", 101L);
        borrowRecord.put("borrowDate", "2025-01-15");
        borrowHistory.add(borrowRecord);
        when(borrowRepository.getBorrowingHistory(userId)).thenReturn(borrowHistory);
        List<Map<String, Object>> result = borrowService.getBorrowHistory(userId);
        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).get("bookId"));
    }
}


