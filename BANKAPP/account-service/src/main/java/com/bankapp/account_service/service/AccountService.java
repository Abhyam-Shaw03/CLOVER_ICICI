package com.bankapp.account_service.service;

import com.bankapp.account_service.dto.AccountCreateDTO;
import com.bankapp.account_service.dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    void createAccount(AccountCreateDTO dto);

    AccountResponseDTO getAccountByAccountId(Long accountId);

    AccountResponseDTO getAccountByAccountNumber(String accountNumber);

    List<AccountResponseDTO> getAccountsByCustomerId(String customerId);

    List<AccountResponseDTO> getAccountsByCustomerAndType(String customerId, String accountType);
}
