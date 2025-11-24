package com.service;

import com.exceptions.InvalidTransactionException;
import com.schemas.Transaction;
import com.schemas.ValidatedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Service
public class TransactionProcessorService {
    @Autowired
    private KafkaTemplate<String, ValidatedTransaction> kafkaTemplate;
    String topic = "transactions-validated";

    public void validateTransaction(Transaction transaction) {
        ValidatedTransaction validatedTransaction = ValidatedTransaction.newBuilder()
                                                    .setTransactionId(transaction.getTransactionId())
                                                    .setAmount(transaction.getAmount())
                                                    .setCurrency(transaction.getCurrency())
                                                    .setTimestamp(transaction.getTimestamp())
                                                    .setValid(false)
                                                    .build();
        validateAmount(validatedTransaction);
        validateUser(transaction.getUserId());
        CompletableFuture<SendResult<String, ValidatedTransaction>> future = kafkaTemplate.send(topic, validatedTransaction);
        future.whenComplete((result, exception) -> {
            if(exception!=null) {
                System.out.println("Failure:"+exception.getMessage());
            }
            else
                System.out.println("Success with offset:" + result.getRecordMetadata().offset());
        });
    }

    private void validateAmount(ValidatedTransaction validatedTransaction) {
        if(validatedTransaction.getAmount()>0)
            validatedTransaction.setValid(true);
        else
            throw new InvalidTransactionException("Invalid transaction amount: " + validatedTransaction.getAmount(), validatedTransaction.getTransactionId());
    }

}
