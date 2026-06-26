package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project2.dto.AccountDTO;
import com.example.project2.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
        private final AccountService accountService;

    // 회원가입
    @GetMapping("/register")
    public String registerPage() {
        return "account/register";
    }

    // 회원가입 처리 
    @PostMapping("/register")
    public String register(@ModelAttribute AccountDTO accountDTO) {
        accountService.register(accountDTO);
        return "redirect:/auth/login";
    }
}
