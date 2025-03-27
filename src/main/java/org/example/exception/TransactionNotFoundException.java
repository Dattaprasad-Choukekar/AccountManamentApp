package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(String transactionId) {
        super("Transaction not found with ID: " + transactionId);
    }
}
