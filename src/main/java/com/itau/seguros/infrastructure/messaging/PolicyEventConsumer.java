package com.itau.seguros.infrastructure.messaging;

import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.aplication.services.InsurancePolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PolicyEventConsumer {

    private final InsurancePolicyService policyService;

    @KafkaListener(topics = "policy-events", groupId = "insurance-policy-group")
    public void consumePolicyEvent(ConsumerRecord<String, PolicyEvent> record) {
        PolicyEvent event = record.value();
        log.info("Consuming PolicyEvent: {}", event);

        try {
            PolicyStatusEnum newStatus = event.getNewStatus();
            UUID policyId = event.getPolicyId();

            switch (newStatus) {
                case VALIDATED:
                    policyService.setPending(policyId);
                    log.info("Policy {} set to PENDING after validation.", policyId);
                    break;
                case PENDING:
                    log.info("Policy {} is PENDING, awaiting payment and subscription.", policyId);
                    break;
                case APPROVED:
                    policyService.approvePolicy(policyId);
                    log.info("Policy {} approved.", policyId);
                    break;
                case REJECTED:
                    policyService.rejectPolicy(policyId);
                    log.info("Policy {} rejected.", policyId);
                    break;
                case CANCELLED:
                    policyService.cancelPolicy(policyId);
                    log.info("Policy {} canceled.", policyId);
                    break;
                default:
                    log.warn("Unhandled status {} for policy {}", newStatus, policyId);
            }

        } catch (Exception ex) {
            log.error("Error processing PolicyEvent: {} - Exception: {}", event, ex.getMessage(), ex);
        }
    }
}