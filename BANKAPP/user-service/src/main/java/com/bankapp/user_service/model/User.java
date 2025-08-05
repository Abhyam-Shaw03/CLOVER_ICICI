package com.bankapp.user_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users", schema = "user_schema") // specify schema if needed
@Builder
public class User {

    @Id
    @NotBlank(message = "User ID is required")
    @Column(length = 12, nullable = false, unique = true)
    private String userId; // e.g., 'CJO29AJD1234'

    @Column(nullable = false)
    private String password; // encoded password (BCrypt etc.)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Enum: CUSTOMER or EMPLOYEE

    public User() {
    }

    public User(String userId, String password, Role role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}