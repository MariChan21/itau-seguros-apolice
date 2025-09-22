package com.itau.seguros.interfaces.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import com.itau.seguros.aplication.services.InsurancePolicyServiceImpl;
import com.itau.seguros.domain.enuns.CategoryEnum;
import com.itau.seguros.domain.enuns.PaymentMethodEnum;
import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.domain.enuns.SalesChannelEnum;
import com.itau.seguros.domain.model.History;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.interfaces.dto.FraudAnalysisResponseDTO;
import com.itau.seguros.interfaces.dto.HistoryDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyRequestDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyResponseDTO;
import com.itau.seguros.interfaces.mapper.InsurancePolicyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InsurancePolicyControllerTest {

    @Mock
    private InsurancePolicyServiceImpl policyService;

    @Mock
    private InsurancePolicyMapper mapper;

    @InjectMocks
    private InsurancePolicyController controller;

    private InsurancePolicyRequestDTO requestDTO;
    private InsurancePolicyResponseDTO responseDTO;
    private InsurancePolicy policy;
    private UUID policyId;
    private UUID customerId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        policyId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        productId = UUID.randomUUID();

        requestDTO =  new InsurancePolicyRequestDTO();
        requestDTO.setCustomerId(UUID.randomUUID());
        requestDTO.setProductId(UUID.randomUUID());
        requestDTO.setCategory("AUTO");
        requestDTO.setSalesChannel("MOBILE");
        requestDTO.setPaymentMethod("CREDIT_CARD");
        requestDTO.setTotalMonthlyPremiumAmount(new BigDecimal("50.00"));
        requestDTO.setInsuredAmount(new BigDecimal("250000.00"));
        requestDTO.setCoverages(Map.of("Roubo", new BigDecimal("50000")));
        requestDTO.setAssistances(List.of("Guincho"));

        policy = new InsurancePolicy();
        policy.setId(UUID.randomUUID());
        policy.setCustomerId(UUID.randomUUID());
        policy.setProductId(UUID.randomUUID());
        policy.setCategory(CategoryEnum.AUTO);
        policy.setSalesChannel(SalesChannelEnum.MOBILE);
        policy.setInsuredAmount(new BigDecimal("250000.00"));
        policy.setStatus(PolicyStatusEnum.RECEIVED);
        policy.setPaymentMethod(PaymentMethodEnum.CREDIT_CARD);
        policy.setCreatedAt(LocalDateTime.now());
        policy.setTotalMonthlyPremiumAmount(BigDecimal.TEN);
        policy.setCoverages(new HashMap<>());
        policy.setAssistances(new ArrayList<>());

        responseDTO = InsurancePolicyResponseDTO.builder()
                .id(policyId)
                .customerId(customerId)
                .productId(productId)
                .category("AUTO")
                .salesChannel("MOBILE")
                .paymentMethod("CREDIT_CARD")
                .status("RECEIVED")
                .createdAt(LocalDateTime.now())
                .finishedAt(null)
                .totalMonthlyPremiumAmount(BigDecimal.valueOf(500.00))
                .insuredAmount(BigDecimal.valueOf(10000.00))
                .coverages(Map.of("Collision", BigDecimal.valueOf(10000), "Theft", BigDecimal.valueOf(5000)))
                .assistances(List.of("Roadside", "Medical"))
                .history(List.of(new HistoryDTO("RECEIVED", LocalDateTime.now().toString())))
                .build();
    }

    @Test
    void testCreatePolicy() {
        when(mapper.toModel(requestDTO)).thenReturn(policy);
        when(policyService.createPolicy(policy)).thenReturn(policy);
        when(mapper.toResponseDto(Optional.of(policy))).thenReturn(responseDTO);

        InsurancePolicyResponseDTO result = controller.create(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getCustomerId(), result.getCustomerId());
        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertEquals(responseDTO.getCoverages(), result.getCoverages());

        verify(mapper).toModel(requestDTO);
        verify(policyService).createPolicy(policy);
        verify(mapper).toResponseDto(Optional.of(policy));
    }

    @Test
    void testValidatePolicy() {
        FraudAnalysisResponseDTO fraud = new FraudAnalysisResponseDTO();
        fraud.setClassification("APPROVED");

        when(policyService.validatePolicy(policyId, "APPROVED")).thenReturn(policy);
        when(mapper.toResponseDto(Optional.of(policy))).thenReturn(responseDTO);

        InsurancePolicyResponseDTO result = controller.validate(policyId, fraud);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getStatus(), result.getStatus());

        verify(policyService).validatePolicy(policyId, "APPROVED");
        verify(mapper).toResponseDto(Optional.of(policy));
    }

    @Test
    void testGetPolicy() {
        when(policyService.findById(policyId)).thenReturn(Optional.of(policy));
        when(mapper.toResponseDto(Optional.of(policy))).thenReturn(responseDTO);

        InsurancePolicyResponseDTO result = controller.getPolicy(policyId);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getCustomerId(), result.getCustomerId());

        verify(policyService).findById(policyId);
        verify(mapper).toResponseDto(Optional.of(policy));
    }
}
