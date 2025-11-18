package com.listener;

import com.entity.Transaction;
import org.springframework.kafka.support.Acknowledgment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TransactionKafkaListener {
    @KafkaListener(topics = "transactions-input", groupId = "paystream-processor-group-v7")
    public void listen(@Payload String transaction) {
        System.out.println("Received a new kafka message:");
        System.out.println(transaction);
    }

    //@KafkaListener(topics = "transactions-input", groupId = "paystream-processor-group-v6", containerFactory = "kafkaListenerContainerFactory")
    public void listenRaw(@Payload ConsumerRecord<byte[], byte[]> record, Acknowledgment acknowledgment) {
        // Convert the value bytes to a String for viewing
        String value = new String(record.value());
        System.out.println("SUCCESS! RECEIVED RAW MESSAGE. Key: " + record.key() + ", Value: " + value);
        acknowledgment.acknowledge();
    }
}
