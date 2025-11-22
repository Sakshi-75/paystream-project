package com.service;

import com.exceptions.InvalidTransactionException;
import com.model.ValidatedTransaction;
import com.schemas.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class TransactionProcessorService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    String topic = "transactions-validated";

    public void validateTransaction(Transaction transaction) {
        ValidatedTransaction validatedTransaction = new ValidatedTransaction();
        validatedTransaction.setTransaction(transaction);
        validatedTransaction.setValid(false);
        validateAmount(validatedTransaction);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, validatedTransaction.toString());
        future.whenComplete((result, exception) -> {
            if(exception!=null) {
                System.out.println("Failure:"+exception.getMessage());
            }
            else
                System.out.println("Success with offset:" + result.getRecordMetadata().offset());
        });
    }

    private void validateAmount(ValidatedTransaction validatedTransaction) {
        if(validatedTransaction.getTransaction().getAmount()>0)
            validatedTransaction.setValid(true);
        else
            throw new InvalidTransactionException("Invalid transaction amount: " + validatedTransaction.getTransaction().getAmount(), validatedTransaction.getTransaction().getTransactionId());
    }

}
