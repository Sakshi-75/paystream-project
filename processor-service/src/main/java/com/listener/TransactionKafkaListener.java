package com.listener;

import com.config.DlqMetrics;
import com.exceptions.InvalidTransactionException;
import com.schemas.Transaction;
import com.service.TransactionProcessorService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.support.Acknowledgment;

@Component
public class TransactionKafkaListener {
    @Autowired
    TransactionProcessorService transactionProcessorService;
    @Autowired
    KafkaTemplate<String, String> dlqKafkaTemplate;
    @Autowired
    DlqMetrics dlqMetrics;

    @Transactional("kafkaTransactionManager")
    @KafkaListener(topics = "transactions-input", groupId = "paystream-processor-group-v7")
    public void listen(ConsumerRecord<String, Transaction> record, Acknowledgment acknowledgment) {
        Transaction transaction = record.value();
        System.out.println("Received a new kafka message:");
        System.out.println(transaction);
        try {
            transactionProcessorService.validateTransaction(transaction);
            acknowledgment.acknowledge();
        } catch (InvalidTransactionException e) {
            System.out.println("Invalid transaction encountered: " + e.getMessage() + ". Routing to DLQ.");
            dlqKafkaTemplate.send("transactions-dlq", (transaction+" | Error: "+ e.getMessage()));
            dlqMetrics.increment();
            acknowledgment.acknowledge();
        }
        catch (Exception e) {
            System.out.println("Processing failed for transaction "+ transaction + ". Routing to DLQ: " + e);
            dlqKafkaTemplate.send("transactions-dlq", (transaction+" | Error: "+ e.getMessage()));
            dlqMetrics.increment();
            throw new RuntimeException("Unrecoverable error, triggering transaction rollback.", e);
        }
    }
}
