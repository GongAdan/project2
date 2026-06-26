package com.example.project2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.project2.service.AccountService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class initDataConfig {

    private final AccountService accountService;
    
    // Spring Boot가 실행될 때 딱 한 번 실행되는 초기 데이터 생성 코드 
    @Bean
    public CommandLineRunner init() {
        return args -> {
            accountService.createAdmin();
        };
    }
}
