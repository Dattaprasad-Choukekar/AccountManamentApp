package org.example.dto;

import org.example.model.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AccountDto {
    private final String accountId;
    private final List<Transaction> transactions;

    public AccountDto(String accountId, List<Transaction> transactions) {
        this.accountId = accountId;
        this.transactions = transactions;
    }

    public String getAccountId() {
        return accountId;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto account = (AccountDto) o;
        return Objects.equals(accountId, account.accountId) && Objects.equals(transactions, account.transactions);
    }

   /* @Override
    public int hashCode() {
        return Objects.hash(accountId, transactions);
    }*/
}
