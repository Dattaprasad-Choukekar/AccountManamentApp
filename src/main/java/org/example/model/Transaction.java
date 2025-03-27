package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Transaction {

    public enum TransactionType {
        DEBIT, CREDIT
    }

    @Id
    @GeneratedValue
    private Long id;
    private String accountId;
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;

    public Transaction() {
    }

    public Transaction(String accountId, String transactionId, BigDecimal amount, TransactionType transactionType) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = transactionType;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(transactionId, that.transactionId) && Objects.equals(amount, that.amount) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, transactionId, amount, type);
    }
}

