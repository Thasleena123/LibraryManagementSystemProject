package com.example.LibraryManagementSystem.controler;

import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.dto.LoginEntity;
import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.services.BookService;
import com.example.LibraryManagementSystem.services.BorrowService;
import com.example.LibraryManagementSystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")

public class UserController {
    private final UserService userService;
    private final BorrowService borrowService;
    private final BookService bookService;

    @Autowired
    public UserController(UserService userService, BorrowService borrowService, BookService bookService) {
        this.userService = userService;
        this.borrowService = borrowService;
        this.bookService = bookService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.userCreation(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("user registered successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error:" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginEntity user, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        session = request.getSession(true);
        Map<String, String> response = new HashMap<>();
        try {
            boolean isAuthenticated = userService.authenticateuser(user.getEmail(), user.getPassword(), session);

            if (isAuthenticated) {
                response.put("message", "Login Successful");
                response.put("session_id", session.getId());
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Invalid email or password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (RuntimeException e) {
            response.put("Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestParam("user_id") int userId, @RequestParam("book_id") Long bookId, @RequestParam("max_days") int maxDays) {
        try {
            borrowService.borrowBook(userId, bookId, maxDays);
            return ResponseEntity.ok("Book borrowed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam("borrowId") Long borrowId) {
        try {
            borrowService.returnBook(borrowId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/borrow-history")
    public ResponseEntity<List<Map<String, Object>>> getBorrowHistory(@RequestParam("userid") int userId) {
        List<Map<String, Object>> history = borrowService.getBorrowHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestParam("author_name") String author) {
        List<Book> books = bookService.fetchBookByAuthor(author);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(books);
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Book>> getBooksByCategory(@RequestParam("category") String category) {
        List<Book> books = bookService.findBookByCategory(category);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(books);
        }
        return ResponseEntity.ok(books);
    }
}

