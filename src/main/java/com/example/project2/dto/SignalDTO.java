package com.example.project2.dto;

import java.time.LocalDateTime;

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
    private String sessionId;
    private Long jobId;
    private String jobName;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;  
}
