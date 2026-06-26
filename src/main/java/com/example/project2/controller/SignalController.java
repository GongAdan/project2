package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project2.service.SignalService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signals")
@RequiredArgsConstructor
public class SignalController {

    private final SignalService signalService;

    @PostMapping("/{jobId}/on")
    public String signalOn(
            @PathVariable Long jobId,
            HttpSession session) {

        signalService.signalOn(jobId, session.getId());

        return "redirect:/jobs/" + jobId;
    }

    @PostMapping("/off")
    public String signalOff(HttpSession session) {

        signalService.signalOff(session.getId());

        return "redirect:/";
    }
}