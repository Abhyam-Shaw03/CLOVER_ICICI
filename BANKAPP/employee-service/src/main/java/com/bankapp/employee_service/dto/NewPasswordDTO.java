package com.bankapp.employee_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


public class NewPasswordDTO {

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public NewPasswordDTO() {
    }

    public NewPasswordDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "NewPasswordDTO{" +
                "newPassword='" + newPassword + '\'' +
                '}';
    }
}