package com.bankapp.account_service.controller;

import com.bankapp.account_service.dto.AccountCreateDTO;
import com.bankapp.account_service.dto.AccountResponseDTO;
import com.bankapp.account_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Create a new account for a customer
     */
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @PostMapping("/create")
    public void createAccount(@Valid @RequestBody AccountCreateDTO dto) {
        accountService.createAccount(dto);
    }

    /**
     * Get account details by account ID
     */
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @GetMapping("/accountIdDetails/{accountId}")
    public AccountResponseDTO getAccountByAccountId(@PathVariable Long accountId) {
        AccountResponseDTO acc = accountService.getAccountByAccountId(accountId);
        return acc;
    }

    /**
     * Get account details by account number
     */
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN') or " +
            "(hasRole('CUSTOMER') and @accountSecurity.canAccessAccount(#accountNumber, authentication.principal))")
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountResponseDTO account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }


    /**
     * Get all accounts of a customer
     */
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN') or #customerId == authentication.principal")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByCustomer(@PathVariable String customerId) {
        List<AccountResponseDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Get all accounts of a customer filtered by account type
     */
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN') or #customerId == authentication.principal")
    @GetMapping("/customer/{customerId}/type/{accountType}")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsByCustomerAndType(
            @PathVariable String customerId,
            @PathVariable String accountType) {
        List<AccountResponseDTO> accounts = accountService.getAccountsByCustomerAndType(customerId, accountType);
        return ResponseEntity.ok(accounts);
    }
}
