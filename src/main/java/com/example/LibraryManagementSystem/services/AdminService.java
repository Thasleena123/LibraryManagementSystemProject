package com.example.LibraryManagementSystem.services;

import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.dto.BookRowMapper;
import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.enums.Role;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final EncodingServices encodingServices;
    private final BookRepository bookRepository;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public AdminService(UserRepository userRepository, EncodingServices encodingServices, JdbcTemplate jdbcTemplate,BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.encodingServices = encodingServices;
        this.bookRepository = bookRepository;
        this.jdbcTemplate=jdbcTemplate;
    }

    public boolean authenticateAdmin(String email, String password) {
        User user = userRepository.getUserByEmail(email);

        if (user == null || user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Authentication failed: Admin not found or not authorized.");
        }

        if (!encodingServices.checkPassword(password, user.getPassword())) {
            throw new RuntimeException("Authentication failed due to incorrect password.");
        }

        return true;
    }
    public Book updateBook(Long id, Book bookDetails) throws IllegalArgumentException {
        // Check if the book exists using RowMapper (if the book is not found, JdbcTemplate will throw an exception)
        String query = "SELECT * FROM books WHERE book_id = ?";
        Book existingBook = null;

        try {
            existingBook = jdbcTemplate.queryForObject(query, new Object[]{id}, new BookRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Book not found.");
        }

        // Update book details
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setCategory(bookDetails.getCategory());
        existingBook.setAvailableCopies(bookDetails.getAvailableCopies());
        existingBook.setRare(bookDetails.isRare());

        // Update the book in the database
        String updateQuery = "UPDATE books SET title = ?, author = ?, category = ?, available_copies = ?, is_rare = ? WHERE book_id = ?";
        jdbcTemplate.update(updateQuery, existingBook.getTitle(), existingBook.getAuthor(), existingBook.getCategory(),
                existingBook.getAvailableCopies(), existingBook.isRare(), existingBook.getBookId());

        return existingBook;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public ResponseEntity<Map<String, String>> addBook(Book book) {
        Book existingBook = bookRepository.bookExistsByTitle(book.getTitle());

        if (existingBook == null) {
            if(bookRepository.save(book)) {
                Map<String, String> response = new HashMap<>();
                response.put("Message", "Successfully added a new Book");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            boolean updated = bookRepository.updateAvailableCopies(book.getTitle(), book.getAvailableCopies() + existingBook.getAvailableCopies());

            if (updated) {
                Map<String, String> response = new HashMap<>();
                response.put("Message", "Successfully updated Existing Book Copies ");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("Message", "Failed to update Book");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
