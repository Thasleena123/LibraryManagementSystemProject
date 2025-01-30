package com.example.LibraryManagementSystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Borrow {
    private int borrowId;
    private int userId;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private double fine;
    private String status;

    public Borrow(int borrowId, int userId, int bookId, LocalDate borrowDate, LocalDate returnDate, double fine, String status) {
        this.borrowId = borrowId;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.fine = fine;
        this.status = status;
    }


}
