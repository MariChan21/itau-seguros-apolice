package com.itau.seguros.domain.model;

import com.itau.seguros.domain.enuns.CategoryEnum;
import com.itau.seguros.domain.enuns.PaymentMethodEnum;
import com.itau.seguros.domain.enuns.PolicyStatusEnum;
import com.itau.seguros.domain.enuns.SalesChannelEnum;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicy {
    @Id
    private UUID id;
    private UUID customerId;
    private UUID productId;
    private CategoryEnum category;
    private SalesChannelEnum salesChannel;
    private PaymentMethodEnum paymentMethod;
    private PolicyStatusEnum status;
    @Timestamp
    private LocalDateTime createdAt;
    @Timestamp
    private LocalDateTime finishedAt;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal insuredAmount;
    @Transient
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    @OneToMany(mappedBy = "insurancePolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> history;

    public void addHistory(String status,InsurancePolicy policy) {
        if (this.history == null) {
            this.history = new ArrayList<>();
        }
        History history = new History();
        history.setStatus(status);
        history.setEventDate(LocalDateTime.now());
        history.setInsurancePolicy(policy);
        this.history.add(history);
    }
}
