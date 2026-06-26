package com.example.project2.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project2.common.AccountRole;
import com.example.project2.dto.AccountDTO;
import com.example.project2.entity.Account;
import com.example.project2.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 관리자 계정 생성 메소드
    public void createAdmin() {
        // admin 계정이 존재할 경우
        if (accountRepository.findByUsername("admin").isPresent()) {
            return; // 메소드 종료
        }
        Account admin = Account.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .nickname("관리자")
                .role(AccountRole.ROLE_ADMIN)
                .build();
        accountRepository.save(admin);
    }

    public int register(AccountDTO accountDTO) {
        Account account = Account.builder()
                .username(accountDTO.getUsername())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .nickname(accountDTO.getNickname())
                .role(AccountRole.ROLE_USER)
                .build();
        accountRepository.save(account);
        return 1;
    }
}