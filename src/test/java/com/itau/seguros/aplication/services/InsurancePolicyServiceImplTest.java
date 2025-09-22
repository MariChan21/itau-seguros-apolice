package com.itau.seguros.aplication.services;

import com.itau.seguros.domain.enuns.CategoryEnum;
import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.infrastructure.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsurancePolicyServiceTest {

    @InjectMocks
    private InsurancePolicyService service;

    @Mock
    private InsurancePolicyRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    InsurancePolicy newPolicy(CategoryEnum category, BigDecimal insuredAmount) {
        InsurancePolicy policy = new InsurancePolicy();
        policy.setId(UUID.randomUUID());
        policy.setCustomerId(UUID.randomUUID());
        policy.setProductId(UUID.randomUUID());
        policy.setCategory(category);
        policy.setInsuredAmount(insuredAmount);
        policy.setStatus(PolicyStatusEnum.RECEIVED);
        policy.setCreatedAt(LocalDateTime.now());
        policy.setTotalMonthlyPremiumAmount(BigDecimal.TEN);
        policy.setCoverages(new HashMap<>());
        policy.setAssistances(new ArrayList<>());
        return policy;
    }

    @Test
    @DisplayName("Deve criar apólice com campos iniciais corretos")
    void createPolicy_setsInitialFields() {
        InsurancePolicy input = newPolicy(CategoryEnum.AUTO, new BigDecimal("50000.00"));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy saved = service.createPolicy(input);

        assertNotNull(saved.getId());
        assertEquals(PolicyStatusEnum.RECEIVED, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getHistory());
        assertFalse(saved.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Validação: REGULAR - deve validar apólice")
    void validatePolicy_regularCustomer_valid() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("200000.00"));
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy validated = service.validatePolicy(policy.getId(), "REGULAR");

        assertEquals(PolicyStatusEnum.VALIDATED, validated.getStatus());
    }

    @Test
    @DisplayName("Validação: REGULAR - deve rejeitar apólice")
    void validatePolicy_regularCustomer_rejected() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("400000.00"));
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy validated = service.validatePolicy(policy.getId(), "REGULAR");

        assertEquals(PolicyStatusEnum.REJECTED, validated.getStatus());
    }

    @Test
    @DisplayName("Apenas apólices validadas podem ser pendentes")
    void setPending_onlyWhenValidated() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.VALIDATED);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy pending = service.setPending(policy.getId());

        assertEquals(PolicyStatusEnum.PENDING, pending.getStatus());
    }

    @Test
    @DisplayName("Não pode pendenciar apólice não validada")
    void setPending_throwsIfNotValidated() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.RECEIVED);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));

        assertThrows(IllegalStateException.class, () -> {
            service.setPending(policy.getId());
        });
    }

    @Test
    @DisplayName("Aprovar apólice apenas se pendente")
    void approvePolicy_onlyWhenPending() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.PENDING);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy approved = service.approvePolicy(policy.getId());

        assertEquals(PolicyStatusEnum.APPROVED, approved.getStatus());
        assertNotNull(approved.getFinishedAt());
    }

    @Test
    @DisplayName("Não pode aprovar apólice não pendente")
    void approvePolicy_throwsIfNotPending() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.VALIDATED);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));

        assertThrows(IllegalStateException.class, () -> {
            service.approvePolicy(policy.getId());
        });
    }

    @Test
    @DisplayName("Rejeitar apólice válida")
    void rejectPolicy_valid() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.VALIDATED);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy rejected = service.rejectPolicy(policy.getId());

        assertEquals(PolicyStatusEnum.REJECTED, rejected.getStatus());
        assertNotNull(rejected.getFinishedAt());
    }

    @Test
    @DisplayName("Não pode rejeitar apólice já aprovada")
    void rejectPolicy_throwsIfApproved() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.APPROVED);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));

        assertThrows(IllegalStateException.class, () -> {
            service.rejectPolicy(policy.getId());
        });
    }

    @Test
    @DisplayName("Cancelar apólice válida")
    void cancelPolicy_valid() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.PENDING);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        InsurancePolicy canceled = service.cancelPolicy(policy.getId());

        assertEquals(PolicyStatusEnum.CANCELLED, canceled.getStatus());
        assertNotNull(canceled.getFinishedAt());
    }

    @Test
    @DisplayName("Não pode cancelar apólice já aprovada")
    void cancelPolicy_rejectedIfApproved() {
        InsurancePolicy policy = newPolicy(CategoryEnum.AUTO, new BigDecimal("100000.00"));
        policy.setStatus(PolicyStatusEnum.APPROVED);
        when(repository.findById(policy.getId())).thenReturn(Optional.of(policy));

        assertThrows(IllegalStateException.class, () -> {
            service.cancelPolicy(policy.getId());
        });
    }
}