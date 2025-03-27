package org.example.service;

import org.example.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto getTransaction(String transactionId);
    List<TransactionDto> getTransactionsByAccountId(String accountId);
    TransactionDto createTransaction(TransactionDto transactionDto);
}
