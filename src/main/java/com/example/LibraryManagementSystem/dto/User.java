package com.example.LibraryManagementSystem.dto;

import com.example.LibraryManagementSystem.enums.Membership;
import com.example.LibraryManagementSystem.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;

    @NotNull(message = "email field cannot be null")
    @Size(max = 100, message = "email must not exceed 100 characters ")
    @Email(message = "invalid email format")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,20}$",
            message = "Password must be 8-20 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;

    @NotNull(message = "Role is required")
//   @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull(message = "name field cannot be null")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "name must be in character format")
    private String firstName;

    private String lastName;

    @NotNull(message = "membership is required")
    private Membership membership;

    public User(int userId, String email, String password, String role, String firstName, String lastName, String membership) {
    }
}
