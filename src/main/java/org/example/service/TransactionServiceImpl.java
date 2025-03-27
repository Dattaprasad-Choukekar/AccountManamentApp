package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.TransactionDto;
import org.example.exception.AccountNotFoundException;
import org.example.exception.TransactionNotFoundException;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private ModelMapper mapper = new ModelMapper();

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    private void init() {
        mapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setAmbiguityIgnored(true);


    }

    @Override
    public TransactionDto getTransaction(String transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        return toTransactionDto(transactionOpt.orElseThrow(() -> new TransactionNotFoundException(transactionId)));
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountId(String accountId) {
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new AccountNotFoundException("Account with does not exist: " + accountId);
        }
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream().map(this::toTransactionDto).collect(Collectors.toList());
    }

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        if (!accountRepository.existsByAccountId(transactionDto.getAccountId())) {
            throw new AccountNotFoundException("Account with does not exist: " + transactionDto.getAccountId());
        }
        Transaction transactionToSave = mapper.map(transactionDto, Transaction.class);
        transactionToSave.setTransactionId(UUID.randomUUID().toString());

        return toTransactionDto(transactionRepository.save(transactionToSave));
    }

    private TransactionDto toTransactionDto(Transaction transaction) {
        return mapper.map(transaction, TransactionDto.class);
    }
}
