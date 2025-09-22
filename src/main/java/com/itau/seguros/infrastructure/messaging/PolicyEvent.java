package com.itau.seguros.infrastructure.messaging;

import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyEvent {
    private UUID policyId;
    private PolicyStatusEnum newStatus;
    private LocalDateTime eventDate;
    private String correlationId;
    private String details; // Pode ser mensagem adicional
}