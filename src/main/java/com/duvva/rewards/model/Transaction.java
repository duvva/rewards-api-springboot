package com.duvva.rewards.model;

import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private Long customerId;
    private double amount;
    private LocalDateTime transactionDate;
    public Transaction() {
        // Default constructor needed for Jackson
    }
    public Transaction(Long id, Long customerId, double amount, LocalDateTime transactionDate) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public double getAmount() { return amount; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
}
