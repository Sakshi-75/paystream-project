package com.listener;

import com.service.TransactionProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionKafkaListener {
    @Autowired
    TransactionProcessorService transactionProcessorService;
    @Autowired
    KafkaTemplate<String, String> dlqKafkaTemplate;

    @Transactional("kafkaTransactionManager")
    @KafkaListener(topics = "transactions-input", groupId = "paystream-processor-group-v7")
    public void listen(@Payload String transaction) {
        System.out.println("Received a new kafka message:");
        System.out.println(transaction);
        try {
            transactionProcessorService.validateTransaction(transaction);
        } catch (Exception e) {
            System.out.println("Processing failed for transaction "+ transaction + ". Routing to DLQ: " + e);
            dlqKafkaTemplate.send("transactions-dlq", (transaction+" | Error: "+ e.getMessage()));
            throw new RuntimeException("Unrecoverable error, triggering transaction rollback.", e);
        }
    }
}
