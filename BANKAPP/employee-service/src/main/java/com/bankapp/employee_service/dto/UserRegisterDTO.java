package com.bankapp.employee_service.dto;

import lombok.Data;


public class UserRegisterDTO {
    private String userId;
    private String password; // this will be already encoded
    private String role;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String userId, String password, String role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String employeeId) {
        this.userId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRegisterDTO{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

