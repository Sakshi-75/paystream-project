package com.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class DlqMetrics {

    private final Counter dlqCounter;

    public DlqMetrics(MeterRegistry registry) {
        this.dlqCounter = Counter.builder("paystream_dlq_total")
                .description("Number of messages sent to DLQ")
                .register(registry);
    }

    public void increment() {
        dlqCounter.increment();
    }
}