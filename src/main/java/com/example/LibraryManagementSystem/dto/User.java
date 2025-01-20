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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public User(int userId, String email, String password, String role, String firstName, String lastName, String membership) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.membership = Membership.valueOf(membership);
   }
    }

