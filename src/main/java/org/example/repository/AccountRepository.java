package org.example.repository;

import org.example.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);

    Account getReferenceByAccountId(String accountId);
}
