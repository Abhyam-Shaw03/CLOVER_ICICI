package com.bankapp.account_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_account_number", columnNames = "account_number")
        },
        indexes = {
                @Index(name = "idx_customer_id", columnList = "customer_id")
        }
)
@Check(constraints = "balance >= 0 AND account_type IN ('SAVINGS','CURRENT','LOAN') AND status IN ('ACTIVE','INACTIVE','BLOCKED')")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", updatable = false, nullable = false, unique = true)
    private Long accountId;

    @NotBlank(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    @NotBlank(message = "Account number is required")
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @NotNull(message = "Account type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private AccountType accountType;

    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Balance can have up to 15 digits and 2 decimals")
    @Column(name = "balance", nullable = false, precision = 17, scale = 2)
    private BigDecimal balance;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    public Account() {
    }

    public Account(Long accountId, String customerId, String accountNumber, AccountType accountType, BigDecimal balance, AccountStatus status) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}

