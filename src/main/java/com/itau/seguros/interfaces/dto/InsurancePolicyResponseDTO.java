package com.itau.seguros.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class InsurancePolicyResponseDTO {
    private UUID id;
    @JsonProperty("customer_id")
    private UUID customerId;
    @JsonProperty("product_id")
    private UUID productId;
    private String category;
    private String salesChannel;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    @JsonProperty("total_monthly_premium_amount")
    private BigDecimal totalMonthlyPremiumAmount;
    @JsonProperty("insured_amount")
    private BigDecimal insuredAmount;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private List<HistoryDTO> history;
}