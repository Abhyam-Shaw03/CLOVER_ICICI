package com.bankapp.account_service.security;

import com.bankapp.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("accountSecurity")
@RequiredArgsConstructor
public class AccountSecurity {

    private final AccountRepository accountRepository;

    /**
     * Check if the given accountNumber belongs to the logged-in customer.
     */
    public boolean canAccessAccount(String accountNumber, String loggedInCustomerId) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> account.getCustomerId().equals(loggedInCustomerId))
                .orElse(false);
    }
}
