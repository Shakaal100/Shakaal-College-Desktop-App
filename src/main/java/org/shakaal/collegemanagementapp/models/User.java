package org.shakaal.collegemanagementapp.models;

import java.time.LocalDate;

public class User {

    private int userId;

    private String username;

    private String passwordHash;

    private String fullName;

    private String role;

    private String status;

    private LocalDate createdAt;

    // ===========================
    // Empty Constructor
    // ===========================

    public User() {
    }

    // ===========================
    // Full Constructor
    // ===========================

    public User(int userId, String username, String passwordHash, String fullName, String role, String status, LocalDate createdAt) {

                        this.userId = userId;
                        this.username = username;
                        this.passwordHash = passwordHash;
                        this.fullName = fullName;
                        this.role = role;
                        this.status = status;
                        this.createdAt = createdAt;
    }

    // ===========================
    // Getters and Setters
    // ===========================

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return fullName;
    }

}