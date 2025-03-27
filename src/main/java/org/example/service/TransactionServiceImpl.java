package org.example.service;

import org.example.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Override
    public TransactionDto getTransaction(String transactionId) {
        return null;
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountId(String accountId) {
        return List.of();
    }

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        return null;
    }
}
