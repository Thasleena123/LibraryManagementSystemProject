package com.example.LibraryManagementSystem.services;

import com.example.LibraryManagementSystem.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BorrowService {


    private final BorrowRepository borrowRepository;

    @Autowired
    public BorrowService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }

    //borrow limit
    public void borrowBook(int userId, Long bookId, int maxDays) throws Exception {
        int borrowedBooksCount = borrowRepository.getBorrowedBooksCount(userId);
        if (borrowedBooksCount >= 5) {
            throw new Exception("Borrow limit exceeded!");
        }

        int availableCopies = borrowRepository.getAvailableCopies(bookId);
        if (availableCopies <= 0) {
            throw new Exception("Book unavailable.");
        }

        Date borrowDate = new Date(System.currentTimeMillis());
        Date returnDate = new Date(borrowDate.getTime() + (maxDays * 24L * 60L * 60L * 1000L));

        borrowRepository.insertBorrowRecord(userId, bookId, returnDate);
        borrowRepository.updateBookAvailability(bookId, -1);
    }

    public void returnBook(Long borrowId) throws IllegalArgumentException {
        if (!borrowRepository.doesBorrowRecordExist(borrowId)) {
            throw new IllegalArgumentException("Borrow record not found.");
        }


        Date currentDate = new Date();
        double fine = borrowRepository.calculateFine(borrowId, currentDate);

        borrowRepository.updateBorrowRecord(borrowId, fine);


        Long bookId = borrowRepository.getBookIdFromBorrowRecord(borrowId);
        borrowRepository.updateBookAvailability(bookId, 1);
    }

    public List<Map<String, Object>> getBorrowHistory(int userId) {
        return borrowRepository.getBorrowingHistory(userId);
    }
}


