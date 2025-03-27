package org.example.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionDto {
    private String accountId;
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;

    public TransactionDto() {
    }

    public TransactionDto(String accountId, String transactionId, BigDecimal amount, TransactionType transactionType) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDto that = (TransactionDto) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(transactionId, that.transactionId) && Objects.equals(amount, that.amount) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, transactionId, amount, type);
    }
}

