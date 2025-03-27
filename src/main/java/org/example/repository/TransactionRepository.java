package org.example.repository;

import org.example.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByAccount_accountId(String accountId);
    Optional<Transaction> findByTransactionId(String transactionId);
}
