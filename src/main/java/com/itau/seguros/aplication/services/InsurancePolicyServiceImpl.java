package com.itau.seguros.aplication.services;


import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.domain.model.History;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.infrastructure.repository.HistoryRepository;
import com.itau.seguros.infrastructure.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private final InsurancePolicyRepository repository;

    private final HistoryRepository historyRepository;

    public InsurancePolicy createPolicy(InsurancePolicy policy) {
        policy.setId(UUID.randomUUID());
        policy.setStatus(PolicyStatusEnum.RECEIVED);
        policy.setCreatedAt(LocalDateTime.now());
        History history = new History();
        history.setStatus(PolicyStatusEnum.RECEIVED.name());
        history.setEventDate(LocalDateTime.now());
        history.setInsurancePolicy(policy);
        policy.setHistory(List.of(history));
        return repository.save(policy);
    }

    public InsurancePolicy validatePolicy(UUID policyId, String customerType) {
        InsurancePolicy policy = findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("Apólice não encontrada: " + policyId));

        if (customerType.equalsIgnoreCase("REGULAR") && policy.getInsuredAmount().compareTo(new java.math.BigDecimal("300000")) > 0) {
            policy.setStatus(PolicyStatusEnum.REJECTED);
            policy.setFinishedAt(LocalDateTime.now());
            policy.addHistory(PolicyStatusEnum.REJECTED.name(),policy);
        } else {
            policy.setStatus(PolicyStatusEnum.VALIDATED);
            policy.addHistory(PolicyStatusEnum.VALIDATED.name(),policy);
        }
        return repository.save(policy);
    }

    public InsurancePolicy setPending(UUID policyId) {
        InsurancePolicy policy = findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("Apólice não encontrada: " + policyId));

        if (policy.getStatus() != PolicyStatusEnum.VALIDATED) {
            throw new IllegalStateException("Só pode pendenciar apólice validada");
        }
        policy.setStatus(PolicyStatusEnum.PENDING);
        policy.addHistory(PolicyStatusEnum.PENDING.name(),policy);
        return repository.save(policy);
    }

    public InsurancePolicy approvePolicy(UUID policyId) {
        InsurancePolicy policy = findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("Apólice não encontrada: " + policyId));

        if (policy.getStatus() != PolicyStatusEnum.PENDING) {
            throw new IllegalStateException("Só pode aprovar apólice pendente");
        }
        policy.setStatus(PolicyStatusEnum.APPROVED);
        policy.setFinishedAt(LocalDateTime.now());
        policy.addHistory(PolicyStatusEnum.APPROVED.name(),policy);
        return repository.save(policy);
    }

    public InsurancePolicy rejectPolicy(UUID policyId) {
        InsurancePolicy policy = findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("Apólice não encontrada: " + policyId));

        if (policy.getStatus() == PolicyStatusEnum.APPROVED) {
            throw new IllegalStateException("Não pode rejeitar apólice já aprovada");
        }
        policy.setStatus(PolicyStatusEnum.REJECTED);
        policy.setFinishedAt(LocalDateTime.now());
        policy.addHistory(PolicyStatusEnum.REJECTED.name(),policy);
        return repository.save(policy);
    }

    public InsurancePolicy cancelPolicy(UUID policyId) {
        InsurancePolicy policy = findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("Apólice não encontrada: " + policyId));

        if (policy.getStatus() == PolicyStatusEnum.APPROVED) {
            throw new IllegalStateException("Não pode cancelar apólice já aprovada");
        }
        policy.setStatus(PolicyStatusEnum.CANCELLED);
        policy.setFinishedAt(LocalDateTime.now());
        policy.addHistory(PolicyStatusEnum.CANCELLED.name(),policy);
        return repository.save(policy);
    }

    public Optional<InsurancePolicy> findById(UUID policyId) {
        return repository.findById(policyId);
    }

    @Override
    public List<InsurancePolicy> findByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId);
    }
}
