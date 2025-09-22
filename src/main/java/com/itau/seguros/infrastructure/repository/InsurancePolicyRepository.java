package com.itau.seguros.infrastructure.repository;


import com.itau.seguros.domain.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, UUID> {
    List<InsurancePolicy> findByCustomerId(UUID customerId);
}
