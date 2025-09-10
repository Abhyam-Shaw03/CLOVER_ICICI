package com.bankapp.account_service.dto;

import jakarta.validation.constraints.NotBlank;


public class AccountCreateDTO {

    @NotBlank(message = "Customer ID is required")
    private String customerId; // comes from customer-service after registration

    @NotBlank(message = "Account type is required")
    private String accountType; // SAVINGS, CURRENT, LOAN (Enum in account-service)

    @NotBlank(message = "Status is required")
    private String status; // ACTIVE, INACTIVE, BLOCKED (Enum in account-service)

    public AccountCreateDTO() {
    }

    public AccountCreateDTO(String customerId, String accountType, String status) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
