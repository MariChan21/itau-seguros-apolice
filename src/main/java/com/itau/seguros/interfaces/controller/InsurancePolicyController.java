package com.itau.seguros.interfaces.controller;

import com.itau.seguros.aplication.services.InsurancePolicyService;
import com.itau.seguros.domain.model.InsurancePolicy;
import com.itau.seguros.interfaces.dto.FraudAnalysisResponseDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyRequestDTO;
import com.itau.seguros.interfaces.dto.InsurancePolicyResponseDTO;
import com.itau.seguros.interfaces.mapper.InsurancePolicyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class InsurancePolicyController {

    private final InsurancePolicyService policyService;
    private final InsurancePolicyMapper mapper; // injete o mapper

    @PostMapping
    public InsurancePolicyResponseDTO create(@RequestBody InsurancePolicyRequestDTO request) {
        InsurancePolicy policy = mapper.toModel(request);
        InsurancePolicy created = policyService.createPolicy(policy);
        return mapper.toResponseDto(Optional.ofNullable(created));
    }

    @PostMapping("/{id}/validate")
    public InsurancePolicyResponseDTO validate(@PathVariable UUID id, @RequestBody FraudAnalysisResponseDTO fraud) {
        InsurancePolicy validated = policyService.validatePolicy(id, fraud.getClassification());
        return mapper.toResponseDto(Optional.ofNullable(validated));
    }

    @GetMapping("/{id}")
    public InsurancePolicyResponseDTO getPolicy(@PathVariable UUID id) {
        Optional<InsurancePolicy> policy = policyService.findById(id);
        return mapper.toResponseDto(policy);
    }
}