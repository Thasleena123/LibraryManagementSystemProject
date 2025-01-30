package com.example.LibraryManagementSystem.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.enums.Role;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.services.AdminService;
import com.example.LibraryManagementSystem.services.EncodingServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;

    class AdminServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private EncodingServices encodingServices;

        @Mock
        private BookRepository bookRepository;

        @Mock
        private JdbcTemplate jdbcTemplate;

        @InjectMocks
        private AdminService adminService;

        private User adminUser;
        private Book book;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            adminUser = new User(1, "admin@example.com", "Password@123", String.valueOf(Role.ADMIN), "Admin", "User", "PREMIUM");
            book = new Book(1, "Java Basics", "John Doe", "Programming", 5, false,3);
        }

        @Test
        void testAuthenticateAdmin_Success() {
            when(userRepository.getUserByEmail("admin@example.com")).thenReturn(adminUser);
            when(encodingServices.checkPassword("Password@123", adminUser.getPassword())).thenReturn(true);

            assertTrue(adminService.authenticateAdmin("admin@example.com", "Password@123"));
        }

        @Test
        void testAuthenticateAdmin_UserNotFound() {
            when(userRepository.getUserByEmail("wrong@example.com")).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> adminService.authenticateAdmin("wrong@example.com", "Password@123"));

            assertEquals("Authentication failed: Admin not found or not authorized.", exception.getMessage());
        }

        @Test
        void testAuthenticateAdmin_IncorrectPassword() {
            when(userRepository.getUserByEmail("admin@example.com")).thenReturn(adminUser);
            when(encodingServices.checkPassword("WrongPassword", adminUser.getPassword())).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> adminService.authenticateAdmin("admin@example.com", "WrongPassword"));

            assertEquals("Authentication failed due to incorrect password.", exception.getMessage());
        }

        @Test
        void testUpdateBook_Success() {
            when(jdbcTemplate.queryForObject(anyString(), (Object[]) any(Object[].class), (RowMapper<Object>) any())).thenReturn(book);

            Book updatedBook = new Book(1, "Advanced Java", "John Doe", "Programming", 10, true,3);
            when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(1);

            Book result = adminService.updateBook(1L, updatedBook);

            assertEquals("Advanced Java", result.getTitle());
            assertEquals(10, result.getAvailableCopies());
            assertTrue(result.isRare());
        }

        @Test
        void testUpdateBook_NotFound() {
            when(jdbcTemplate.queryForObject(anyString(), (Object[]) any(Object[].class), (RowMapper<Object>) any())).thenThrow(new EmptyResultDataAccessException(1));

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> adminService.updateBook(1L, book));

            assertEquals("Book not found.", exception.getMessage());
        }

        @Test
        void testGetAllUsers() {
            List<User> users = Arrays.asList(adminUser);
            when(userRepository.getAllUsers()).thenReturn(users);

            List<User> result = adminService.getAllUsers();
            assertEquals(1, result.size());
            assertEquals("admin@example.com", result.get(0).getEmail());
        }

        @Test
        void testAddBook_NewBook_Success(){}

    }
