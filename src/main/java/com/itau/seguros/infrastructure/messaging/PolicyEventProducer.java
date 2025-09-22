package com.itau.seguros.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyEventProducer {

    private final KafkaTemplate<String, PolicyEvent> kafkaTemplate;
    private static final String TOPIC = "policy-events";

    public void publishPolicyEvent(PolicyEvent event) {
        kafkaTemplate.send(TOPIC, event.getPolicyId().toString(), event);
    }
}