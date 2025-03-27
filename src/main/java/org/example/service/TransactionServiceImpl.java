package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.TransactionDto;
import org.example.exception.AccountNotFoundException;
import org.example.exception.TransactionNotFoundException;
import org.example.model.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
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
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<Transaction, TransactionDto> accountIdMapper = mapper.createTypeMap(Transaction.class, TransactionDto.class);
        accountIdMapper.addMappings(mapper
                -> mapper.map(t -> t.getAccount().getAccountId(), TransactionDto::setAccountId)
        );
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
        List<Transaction> transactions = transactionRepository.findByAccount_accountId(accountId);
        return transactions.stream().map(this::toTransactionDto).collect(Collectors.toList());
    }

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        if (!accountRepository.existsByAccountId(transactionDto.getAccountId())) {
            throw new AccountNotFoundException("Account with does not exist: " + transactionDto.getAccountId());
        }
        Transaction transactionToSave = mapper.map(transactionDto, Transaction.class);
        transactionToSave.setAccount(accountRepository.getReferenceByAccountId(transactionDto.getAccountId()));
        transactionToSave.setTransactionId(UUID.randomUUID().toString());

        return toTransactionDto(transactionRepository.save(transactionToSave));
    }

    private TransactionDto toTransactionDto(Transaction transaction) {
        return mapper.map(transaction, TransactionDto.class);
    }
}
