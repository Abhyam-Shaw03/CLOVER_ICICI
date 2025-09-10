package com.bankapp.account_service.service;

import com.bankapp.account_service.dto.AccountCreateDTO;
import com.bankapp.account_service.dto.AccountResponseDTO;
import com.bankapp.account_service.exception.DuplicateAccountTypeException;
import com.bankapp.account_service.model.*;
import com.bankapp.account_service.mapper.AccountMapper;
import com.bankapp.account_service.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public void createAccount(AccountCreateDTO dto) {
        // Let mapper handle conversion
        Account account = accountMapper.toEntity(dto);

        // Prevent duplicate accounts of the same type for the customer
        if (accountRepository.existsByCustomerIdAndAccountType(dto.getCustomerId(), account.getAccountType())) {
            throw new DuplicateAccountTypeException(
                    "Customer " + dto.getCustomerId() + " already has an account of type " + account.getAccountType()
            );
        }

        // Set additional fields
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);
    }

    @Override
    public AccountResponseDTO getAccountByAccountId(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        return account != null ? accountMapper.toResponseDTO(account) : null;
    }

    @Override
    public AccountResponseDTO getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with number: " + accountNumber));
        return accountMapper.toResponseDTO(account);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDTO> getAccountsByCustomerAndType(String customerId, String accountType) {
        AccountType typeEnum = AccountType.valueOf(accountType.toUpperCase());
        return accountRepository.findByCustomerIdAndAccountType(customerId, typeEnum)
                .stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%012d", (long) (Math.random() * 1_000_000_000_000L));
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
}
