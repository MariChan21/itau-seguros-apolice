package com.itau.seguros.interfaces.mapper;


import com.itau.seguros.domain.enuns.CategoryEnum;
import com.itau.seguros.domain.enuns.PaymentMethodEnum;
import com.itau.seguros.domain.enuns.SalesChannelEnum;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.interfaces.dto.HistoryDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyRequestDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InsurancePolicyMapper {

    public InsurancePolicy toModel(InsurancePolicyRequestDTO dto) {
        InsurancePolicy entity = new InsurancePolicy();
        entity.setCustomerId(dto.getCustomerId());
        entity.setProductId(dto.getProductId());
        entity.setCategory(CategoryEnum.valueOf(dto.getCategory().toUpperCase()));
        entity.setSalesChannel(SalesChannelEnum.valueOf(dto.getSalesChannel().toUpperCase()));
        entity.setPaymentMethod(PaymentMethodEnum.valueOf(dto.getPaymentMethod().toUpperCase()));
        entity.setTotalMonthlyPremiumAmount(dto.getTotalMonthlyPremiumAmount());
        entity.setInsuredAmount(dto.getInsuredAmount());
        entity.setCoverages(dto.getCoverages());
        entity.setAssistances(dto.getAssistances());
        return entity;
    }

    public InsurancePolicyResponseDTO toResponseDto(Optional<InsurancePolicy> model) {
        InsurancePolicy policy = model.orElseThrow(() -> new IllegalArgumentException("Apólice não encontrada"));

        return InsurancePolicyResponseDTO.builder()
                .id(policy.getId())
                .customerId(policy.getCustomerId())
                .productId(policy.getProductId())
                .category(policy.getCategory().name())
                .salesChannel(policy.getSalesChannel().name())
                .paymentMethod(policy.getPaymentMethod().name())
                .status(policy.getStatus().name())
                .createdAt(policy.getCreatedAt())
                .finishedAt(policy.getFinishedAt())
                .totalMonthlyPremiumAmount(policy.getTotalMonthlyPremiumAmount())
                .insuredAmount(policy.getInsuredAmount())
                .coverages(policy.getCoverages())
                .assistances(policy.getAssistances())
                .history(policy.getHistory() != null
                        ? policy.getHistory().stream()
                        .map(h -> HistoryDTO.builder()
                                .status(h.getStatus())
                                .timestamp(String.valueOf(h.getEventDate()))
                                .build())
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}