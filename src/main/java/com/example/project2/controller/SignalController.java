package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project2.service.JobService;
import com.example.project2.service.SignalService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signals")
@RequiredArgsConstructor
public class SignalController {

    private final SignalService signalService;
    private final JobService jobService;

    @GetMapping("/match")
    public String match(@RequestParam(required = false) Long jobId,
            Model model) {

        model.addAttribute("jobs", jobService.getJobs());

        if (jobId != null) {
            model.addAttribute("signals", signalService.getSignals(jobId));
        }

        return "signal/match";
    }

    @PostMapping("/on")
    public String on(@RequestParam Long jobId,
            HttpSession session) {

        signalService.activate(jobId, session);

        return "redirect:/signal/match?jobId=" + jobId;
    }

    @PostMapping("/off")
    public String off(HttpSession session) {

        signalService.deactivate(session);

        Long jobId = (Long) session.getAttribute("jobId");

        if (jobId == null) {
            return "redirect:/signal";
        }

        return "redirect:/signal/" + jobId;
    }
}