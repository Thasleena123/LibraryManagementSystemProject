package com.example.LibraryManagementSystem.dto;
import lombok.Data;


public class Book {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private int maxBorrowDays;
    private int availableCopies;
    private boolean isRare;

    public Book(int bookId, String title, String author, String category, int availableCopies, boolean isRare, int maxBorrowDays) {

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availableCopies = availableCopies;
        this.isRare = isRare;
        this.maxBorrowDays = maxBorrowDays;
    }

    public Book() {

    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isRare() {
        return isRare;
    }

    public void setRare(boolean rare) {
        isRare = rare;
    }

    public int getMaxBorrowDays() {
        return maxBorrowDays;
    }

    public void setMaxBorrowDays(int maxBorrowDays) {
        this.maxBorrowDays = maxBorrowDays;
    }


}
