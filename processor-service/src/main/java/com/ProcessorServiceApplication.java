package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ProcessorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessorServiceApplication.class, args);
    }

}