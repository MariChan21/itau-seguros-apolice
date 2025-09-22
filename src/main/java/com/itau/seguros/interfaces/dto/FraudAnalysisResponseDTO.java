package com.itau.seguros.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class FraudAnalysisResponseDTO {
    @JsonProperty("orderId")
    private UUID orderId;
    @JsonProperty("customerId")
    private UUID customerId;
    @JsonProperty("analyzedAt")
    private LocalDateTime analyzedAt;
    @JsonProperty("classification")
    private String classification;
    @JsonProperty("occurrences")
    private List<Occurrence> occurrences;

    @Data
    public static class Occurrence {
        private UUID id;
        private Long productId;
        private String type;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}