package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.project2.common.JobRole;
import com.example.project2.service.JobService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final JobService jobService;

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("tanks",
                jobService.getJobsByRole(JobRole.TANK));

        model.addAttribute("healers",
                jobService.getJobsByRole(JobRole.HEALER));

        model.addAttribute("melees",
                jobService.getJobsByRole(JobRole.MELEE));

        model.addAttribute("rangeds",
                jobService.getJobsByRole(JobRole.RANGED));

        model.addAttribute("casters",
                jobService.getJobsByRole(JobRole.CASTER));

        return "main/index";
    }

}