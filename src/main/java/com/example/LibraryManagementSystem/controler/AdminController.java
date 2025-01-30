package com.example.LibraryManagementSystem.controler;

import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.services.AdminService;
import com.example.LibraryManagementSystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        try {
            boolean isAuthenticated = adminService.authenticateAdmin(email, password);
            if (isAuthenticated) {
                return ResponseEntity.ok("admin login successfull");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid email.password");
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error :" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/books/add")
    public ResponseEntity<Map<String,String>> addBook(@RequestBody Book book) {
        return adminService.addBook(book);
    }
    @PutMapping("/books/update/{id}")
    public ResponseEntity<Map<String, String>> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        try {
            Book updatedBook = adminService.updateBook(id, book);
            return ResponseEntity.ok(Map.of("message", "Book updated successfully", "book", updatedBook.toString()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Book not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Error: " + e.getMessage()));
        }
    }
}




