package com.example.LibraryManagementSystem.service;




import com.example.LibraryManagementSystem.dto.Book;
import com.example.LibraryManagementSystem.dto.BookRowMapper;
    import com.example.LibraryManagementSystem.repository.BookRepository;
    import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

@JdbcTest
@TestPropertySource("classpath:application-test.properties")
public class BookRepositoryTest {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @InjectMocks
        private BookRepository bookRepository;

        private Book testBook;

        @BeforeEach
        void setUp() {
            // Set up the book object to be used for testing
            testBook = new Book(1, "Java Basics", "John Doe", "Programming", 5, false, 7);

            // Set up the in-memory database for testing
            String createTableSql = "CREATE TABLE Books (" +
                    "book_id INT PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "author VARCHAR(255), " +
                    "category VARCHAR(255), " +
                    "available_copies INT, " +
                    "is_rare BOOLEAN, " +
                    "max_borrow_days INT)";
            jdbcTemplate.execute(createTableSql);  // Create table before tests

            // Insert test data
            String insertSql = "INSERT INTO Books (book_id ,title, author, category, available_copies, is_rare, max_borrow_days) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, testBook.getBookId(), testBook.getTitle(), testBook.getAuthor(), testBook.getCategory(),
                    testBook.getAvailableCopies(), testBook.isRare(), testBook.getMaxBorrowDays());
        }

        @Test
        void testSaveBook_Success() {
            // Arrange: Prepare book object
            Book newBook = new Book(2, "Spring Boot", "Jane Smith", "Programming", 3, false, 14);

            // Act: Save the book using the repository
            boolean result = bookRepository.save(newBook);

            // Assert: Verify that the book was saved successfully
            assertTrue(result);

            // Verify the book is in the database
            Book savedBook = jdbcTemplate.queryForObject("SELECT * FROM Books WHERE title = 'Spring Boot'", new BookRowMapper());
            assertNotNull(savedBook);
            assertEquals("Spring Boot", savedBook.getTitle());
        }

        @Test
        void testUpdateAvailableCopies_Success() {
            // Arrange: Prepare the new available copies value
            int newCopies = 10;

            // Act: Update available copies for the test book
            boolean result = bookRepository.updateAvailableCopies("Java Basics", newCopies);

            // Assert: Verify the update was successful
            assertTrue(result);

            // Verify the available copies were updated in the database
            Book updatedBook = jdbcTemplate.queryForObject("SELECT * FROM Books WHERE title = 'Java Basics'", new BookRowMapper());
            assertNotNull(updatedBook);
            assertEquals(newCopies, updatedBook.getAvailableCopies());
        }

        @Test
        void testFindAllBooks_Success() {
            // Arrange: Save more books to test fetch all
            Book anotherBook = new Book(3, "Advanced Java", "John Doe", "Programming", 8, true, 14);
            bookRepository.save(anotherBook);

            // Act: Retrieve all books
            List<Book> books = bookRepository.findAll();

            // Assert: Verify that all books are returned
            assertNotNull(books);
            assertTrue(books.size() > 1);  // There should be at least two books
        }

        @Test
        void testBookExistsByTitle_Found() {
            // Act: Check if the book with the title "Java Basics" exists
            Book foundBook = bookRepository.bookExistsByTitle("Java Basics");

            // Assert: Verify the book is found
            assertNotNull(foundBook);
            assertEquals("Java Basics", foundBook.getTitle());
        }

        @Test
        void testBookExistsByTitle_NotFound() {
            // Act: Check if the book with the title "Nonexistent Book" exists
            Book foundBook = bookRepository.bookExistsByTitle("Nonexistent Book");

            // Assert: Verify the book is not found
            assertNull(foundBook);
        }
    }


