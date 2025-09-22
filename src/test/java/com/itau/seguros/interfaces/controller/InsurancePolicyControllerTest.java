package com.itau.seguros.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.seguros.aplication.services.InsurancePolicyService;
import com.itau.seguros.domain.enuns.CategoryEnum;
import com.itau.seguros.domain.enuns.PaymentMethodEnum;
import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.domain.enuns.SalesChannelEnum;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.interfaces.dto.InsurancePolicyRequestDTO;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.*;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InsurancePolicyController.class)
class InsurancePolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsurancePolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPolicy_returns201() throws Exception {
        InsurancePolicyRequestDTO request = new InsurancePolicyRequestDTO();
        request.setCustomerId(UUID.randomUUID());
        request.setProductId(UUID.randomUUID());
        request.setCategory("AUTO");
        request.setSalesChannel("MOBILE");
        request.setPaymentMethod("CREDIT_CARD");
        request.setTotalMonthlyPremiumAmount(new BigDecimal("100.00"));
        request.setInsuredAmount(new BigDecimal("300000.00"));
        request.setCoverages(Map.of("Perda Total", new BigDecimal("100000")));
        request.setAssistances(List.of("Chaveiro"));

        InsurancePolicy saved = new InsurancePolicy();
        saved.setId(UUID.randomUUID());
        saved.setCustomerId(request.getCustomerId());
        saved.setProductId(request.getProductId());
        saved.setCategory(CategoryEnum.AUTO);
        saved.setSalesChannel(SalesChannelEnum.MOBILE);
        saved.setPaymentMethod(PaymentMethodEnum.CREDIT_CARD);
        saved.setStatus(PolicyStatusEnum.RECEIVED);

        when(policyService.createPolicy(any())).thenReturn(saved);

        mockMvc.perform(post("/policies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.category").value("AUTO"));
    }

}