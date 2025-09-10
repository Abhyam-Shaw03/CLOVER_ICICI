package com.bankapp.account_service.repository;

import com.bankapp.account_service.dto.AccountResponseDTO;
import com.bankapp.account_service.model.Account;
import com.bankapp.account_service.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountId(Long accountId);

    // Find all accounts for a customer
    List<Account> findByCustomerId(String customerId);

    // Find an account by account number (unique constraint ensures only one result)
    Optional<Account> findByAccountNumber(String accountNumber);

    // Find all accounts by type for a customer
    List<Account> findByCustomerIdAndAccountType(String customerId, AccountType accountType);

    // Optional: check if a customer already has an account of a certain type
    boolean existsByCustomerIdAndAccountType(String customerId, AccountType accountType);
}
