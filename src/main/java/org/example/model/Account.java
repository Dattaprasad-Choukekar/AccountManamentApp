package org.example.model;

import java.util.Collections;
import java.util.List;

public class Account {
    private final String accountId;
    private final List<Transaction> transactions;

    public Account(String accountId, List<Transaction> transactions) {
        this.accountId = accountId;
        this.transactions = transactions;
    }

    public String getAccountId() {
        return accountId;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}
