package com.itau.seguros.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "insurance_policy_id")
    private InsurancePolicy insurancePolicy;

    public History(String name, LocalDateTime eventDate) {
    }
}