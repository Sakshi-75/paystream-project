package com.service;

import com.entity.Transaction;
import com.model.UserTransaction;
import com.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    String topic = "transactions-input";

    public int initiateTransaction(UserTransaction userTransaction) {
        Transaction transaction = createTransaction(userTransaction);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, transaction.toString());
        future.whenComplete((result, exception) -> {
            if(exception!=null) {
                System.out.println("Failure:"+exception.getMessage());
            }
            else
                System.out.println("Success with offset:" + result.getRecordMetadata().offset());
        });
        return transaction.getTransactionId();
    }

    public Transaction createTransaction(UserTransaction userTransaction) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userTransaction.user_id());
        transaction.setAmount(userTransaction.amount());
        transaction.setCurrency(userTransaction.currency());
        transaction.setTimestamp(LocalDateTime.now());
        transaction = transactionRepository.save(transaction);
        return transaction;
    }
}
