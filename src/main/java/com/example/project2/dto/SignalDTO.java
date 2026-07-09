package com.example.project2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalDTO {

    private Long signalId;

    private Long jobId;

    private String tier;

    private Integer slotNumber;

    private Boolean active;
}