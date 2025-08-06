package com.sumit.custom_validator;

public record BankAccount(String accountNumber, double balance, String ownerName) {

    // Compact constructor for validation
    public BankAccount {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number can't be empty");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance can't be negative");
        }
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name is required");
        }
    }

    // Custom methods
    public boolean canAfford(double amount) {
        return balance >= amount;
    }

    public BankAccount withdraw(double amount) {
        if (!canAfford(amount)) {
            throw new IllegalStateException("Insufficient funds");
        }
        return new BankAccount(accountNumber, balance - amount, ownerName);
    }

    public BankAccount deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        return new BankAccount(accountNumber, balance + amount, ownerName);
    }
}