package com.bankapp.user_service.dto;

import lombok.Data;

@Data
public class UserLoginResponseDTO {

    private String userId;
    private String role;

    public UserLoginResponseDTO() {
    }

    public UserLoginResponseDTO(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserLoginResponseDTO{" +
                "userId='" + userId + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
