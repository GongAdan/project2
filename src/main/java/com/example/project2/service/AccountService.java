package com.example.project2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project2.common.AccountRole;
import com.example.project2.dto.AccountDTO;
import com.example.project2.entity.Account;
import com.example.project2.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole().name().replace("ROLE_", ""))
                .build();
    }

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