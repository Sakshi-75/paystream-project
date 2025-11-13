package com.service;

import com.entity.Transaction;
import com.model.UserTransaction;
import com.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public int createTransaction(UserTransaction userTransaction) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userTransaction.user_id());
        transaction.setAmount(userTransaction.amount());
        transaction.setCurrency(userTransaction.currency());
        transaction.setTimestamp(LocalDateTime.now());
        transaction = transactionRepository.save(transaction);
        return transaction.getTransactionId();
    }
}
