package com.example.LibraryManagementSystem.service;
import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class BookServiceTest {

        @Mock
        private BookRepository bookRepository;

        @InjectMocks
        private BookService bookService;

        private List<Book> books;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            books = new ArrayList<>();
            books.add(new Book(1, "Java Basics", "John Doe", "Programming", 5, false, 7));
            books.add(new Book(2, "Spring Boot", "John Doe", "Programming", 3, false, 14));
            books.add(new Book(3, "Advanced Java", "Jane Smith", "Programming", 4, true, 10));
            books.add(new Book(4, "Data Structures", "Alice Brown", "Computer Science", 6, false, 21));
        }

        @Test
        void testFetchBookByAuthor_Success() {
            when(bookRepository.findAll()).thenReturn(books);

            List<Book> result = bookService.fetchBookByAuthor("John Doe");

            assertEquals(2, result.size());
            assertEquals("Java Basics", result.get(0).getTitle());
            assertEquals("Spring Boot", result.get(1).getTitle());
        }

        @Test
        void testFetchBookByAuthor_NoBooksFound() {
            when(bookRepository.findAll()).thenReturn(books);
            List<Book> result = bookService.fetchBookByAuthor("Nonexistent Author");
            assertTrue(result.isEmpty());
        }
        @Test
        void testFindBookByCategory_Success() {

            when(bookRepository.findAll()).thenReturn(books);
            List<Book> result = bookService.findBookByCategory("Programming");
            assertEquals(3, result.size());
            assertTrue(result.stream().allMatch(book -> book.getCategory().equals("Programming")));
        }

        @Test
        void testFindBookByCategory_NoBooksFound() {
            when(bookRepository.findAll()).thenReturn(books);
            List<Book> result = bookService.findBookByCategory("Nonexistent Category");
            assertTrue(result.isEmpty());
        }
    }


