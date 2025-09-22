package com.itau.seguros.interfaces.mapper;

import com.itau.seguros.domain.enuns.CategoryEnum;
import com.itau.seguros.domain.enuns.PaymentMethodEnum;
import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.domain.enuns.SalesChannelEnum;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.interfaces.dto.InsurancePolicyRequestDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsurancePolicyMapperTest {
    private final InsurancePolicyMapper mapper = new InsurancePolicyMapper();

    @Test
    void testDtoToEntity() {
        InsurancePolicyRequestDTO dto = new InsurancePolicyRequestDTO();
        dto.setCustomerId(UUID.randomUUID());
        dto.setProductId(UUID.randomUUID());
        dto.setCategory("AUTO");
        dto.setSalesChannel("MOBILE");
        dto.setPaymentMethod("CREDIT_CARD");
        dto.setTotalMonthlyPremiumAmount(new BigDecimal("50.00"));
        dto.setInsuredAmount(new BigDecimal("250000.00"));
        dto.setCoverages(Map.of("Roubo", new BigDecimal("50000")));
        dto.setAssistances(List.of("Guincho"));

        InsurancePolicy model = mapper.toModel(dto);

        assertEquals(CategoryEnum.AUTO, model.getCategory());
        assertEquals(SalesChannelEnum.MOBILE, model.getSalesChannel());
        assertEquals(PaymentMethodEnum.CREDIT_CARD, model.getPaymentMethod());
        assertEquals(dto.getTotalMonthlyPremiumAmount(), model.getTotalMonthlyPremiumAmount());
        assertEquals(dto.getInsuredAmount(), model.getInsuredAmount());
        assertEquals(dto.getCoverages(), model.getCoverages());
        assertEquals(dto.getAssistances(), model.getAssistances());
    }

    @Test
    void testEntityToDto() {
        InsurancePolicy model = new InsurancePolicy();
        model.setId(UUID.randomUUID());
        model.setCustomerId(UUID.randomUUID());
        model.setProductId(UUID.randomUUID());
        model.setCategory(CategoryEnum.AUTO);
        model.setSalesChannel(SalesChannelEnum.MOBILE);
        model.setPaymentMethod(PaymentMethodEnum.CREDIT_CARD);
        model.setTotalMonthlyPremiumAmount(new BigDecimal("100.00"));
        model.setInsuredAmount(new BigDecimal("300000.00"));
        model.setCoverages(Map.of("Perda Total", new BigDecimal("100000")));
        model.setAssistances(List.of("Chaveiro"));
        model.setStatus(PolicyStatusEnum.APPROVED);

        InsurancePolicyResponseDTO dto = mapper.toResponseDto(Optional.of(model));

        assertEquals("AUTO", dto.getCategory());
        assertEquals("MOBILE", dto.getSalesChannel());
        assertEquals("CREDIT_CARD", dto.getPaymentMethod());
        assertEquals(model.getTotalMonthlyPremiumAmount(), dto.getTotalMonthlyPremiumAmount());
        assertEquals(model.getInsuredAmount(), dto.getInsuredAmount());
        assertEquals(model.getCoverages(), dto.getCoverages());
        assertEquals(model.getAssistances(), dto.getAssistances());
    }
}