package com.example.project2.dto;

import java.time.LocalDateTime;

import com.example.project2.common.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private Long accountId;
    private String username;
    private String password;
    private String nickname;
    private LocalDateTime regDate;
    private AccountRole role;
}
