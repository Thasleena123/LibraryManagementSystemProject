package com.example.LibraryManagementSystem.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.example.LibraryManagementSystem.dto.User;
import com.example.LibraryManagementSystem.repository.SessionRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.services.EncodingServices;
import com.example.LibraryManagementSystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncodingServices encodingServices;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1, "test@example.com", "Password@123", "USER", "John", "Doe", "Premium");
    }

    @Test
    void testUserCreation() {
        when(encodingServices.encodePassword(anyString())).thenReturn("encryptedPassword");

        userService.userCreation(user);

        assertEquals("encryptedPassword", user.getPassword());
        verify(userRepository, times(1)).userCreation(user);
    }

    @Test
    void testDeleteUser() {
        int userId = 1;
        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteUser(userId);
    }

    @Test
    void testAuthenticateUser_Success() {
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(user);
        when(encodingServices.checkPassword("Password@123", user.getPassword())).thenReturn(true);
        when(session.getId()).thenReturn("session123");

        boolean result = userService.authenticateuser("test@example.com", "Password@123", session);

        assertTrue(result);
        verify(sessionRepository, times(1)).addOrUpdateSession(user.getUserId(), "session123");
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        when(userRepository.getUserByEmail("wrong@example.com")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticateuser("wrong@example.com", "Password@123", session));

        assertEquals("Authentication failes:users not found", exception.getMessage());
    }

    @Test
    void testAuthenticateUser_IncorrectPassword() {
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(user);
        when(encodingServices.checkPassword("WrongPassword", user.getPassword())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.authenticateuser("test@example.com", "WrongPassword", session));

        assertEquals("authentication failed due to incorrect password", exception.getMessage());
    }
}
