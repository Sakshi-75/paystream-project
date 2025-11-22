package com.exceptions;

public class InvalidTransactionException extends RuntimeException {

    // Unique transaction ID associated with the failure
    private final String transactionId;

    public InvalidTransactionException(String message, String transactionId) {
        super(message);
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}