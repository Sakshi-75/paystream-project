package com.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DLQListener {
    @KafkaListener(topics = "transactions-dlq", groupId = "dlq-auditor-group")
    public void auditDlq(String rawFailedJson) {
        // Logs the raw, failed JSON string for auditing
        System.out.println("DLQ ALERT: Received failed message for audit: {}"+ rawFailedJson);
    }
}
