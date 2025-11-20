package com.model;

import com.entity.Transaction;

public class ValidatedTransaction {
    private Transaction transaction;
    private boolean isValid;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "ValidatedTransaction{" +
                "transaction=" + transaction +
                ", isValid=" + isValid +
                '}';
    }
}
