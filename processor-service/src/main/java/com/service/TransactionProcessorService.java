package com.service;

import com.entity.Transaction;
import com.model.ValidatedTransaction;
import com.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TransactionProcessorService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    String topic = "transactions-validated";

    public void validateTransaction(String transactionString) {
        Integer transactionId = extractTransactionId(transactionString);
        Transaction transaction = transactionRepository.findById(transactionId).get();
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
    }

    private Integer extractTransactionId(String transaction) {
        Pattern pattern = Pattern.compile("transactionId=(\\d+)");
        Matcher matcher = pattern.matcher(transaction);

        if (matcher.find()) {
            String idString = matcher.group(1);
            try {
                return Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Found ID, but could not parse it as an Integer: " + idString);
            }
        } else {
            throw new IllegalArgumentException("Transaction ID pattern 'transactionId=...' not found in the string.");
        }
    }
}
