package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

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
    @ManyToOne
    private Account account;
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;

    public Transaction() {
    }

    public Transaction(Account account, String transactionId, BigDecimal amount,
                       TransactionType transactionType) {
        this.account = account;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = transactionType;
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

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(account, that.account) && Objects.equals(transactionId, that.transactionId) && Objects.equals(amount, that.amount) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, transactionId, amount, type);
    }
}

