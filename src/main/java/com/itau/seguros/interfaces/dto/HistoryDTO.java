package com.itau.seguros.interfaces.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class HistoryDTO {
    private String status;
    private String timestamp;

    public HistoryDTO(String status, String timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }
}
