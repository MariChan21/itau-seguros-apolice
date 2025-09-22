package com.itau.seguros.aplication.services;

import com.itau.seguros.domain.model.InsurancePolicy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface InsurancePolicyService {
    InsurancePolicy createPolicy(InsurancePolicy policy);

    InsurancePolicy validatePolicy(UUID policyId, String riskClassification);

    InsurancePolicy setPending(UUID policyId) ;

    InsurancePolicy approvePolicy(UUID policyId);

    InsurancePolicy rejectPolicy(UUID policyId);

    InsurancePolicy cancelPolicy(UUID policyId);

    Optional<InsurancePolicy> findById(UUID id);

    List<InsurancePolicy> findByCustomerId(UUID customerId) ;

}
