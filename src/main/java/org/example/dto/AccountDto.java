package org.example.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.example.model.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AccountDto {
    private final String accountId;
    private final String email;
    private final List<Transaction> transactions;

    @JsonCreator
    public AccountDto(String accountId, String email, List<Transaction> transactions) {
        this.accountId = accountId;
        this.email = email;
        this.transactions = transactions;
    }

    public AccountDto(String email, List<Transaction> transactions) {
        this(null, email, transactions);
    }

    public String getAccountId() {
        return accountId;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(email, that.email) && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, email, transactions);
    }
}
