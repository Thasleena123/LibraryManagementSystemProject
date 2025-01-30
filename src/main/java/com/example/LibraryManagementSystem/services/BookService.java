package com.example.LibraryManagementSystem.services;

import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> fetchBookByAuthor(String author) {
        List<Book> books = bookRepository.findAll();
        return books.stream().filter(book -> book.getAuthor().equals(author)).toList();
    }

    public List<Book> findBookByCategory(String category) {
        List<Book> books = bookRepository.findAll();
        return books.stream().filter(book -> book.getCategory().equals(category)).toList();
    }

}
