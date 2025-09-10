package com.bankapp.account_service.exception;

public class DuplicateAccountTypeException extends RuntimeException {
    public DuplicateAccountTypeException(String message) {
        super(message);
    }
}
