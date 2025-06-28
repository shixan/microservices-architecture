package com.example.springuserservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Producer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Producer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(String email, String operation) {
        Map<String, String> event = new HashMap<>();
        event.put("email", email);
        event.put("operation", operation);
        kafkaTemplate.send("user-events", event);
    }
}