package org.example.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(String transactionId) {
        super("Transaction not found with ID: " + transactionId);
    }
}
