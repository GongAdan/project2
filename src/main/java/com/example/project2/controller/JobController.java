package com.example.project2.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project2.common.BoardType;
import com.example.project2.dto.BoardDTO;
import com.example.project2.service.BoardService;
import com.example.project2.service.JobService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final BoardService boardService;

    @GetMapping("/{jobId}")
    public String detail(@PathVariable Long jobId, Model model) {

        model.addAttribute("job", jobService.getJob(jobId));

        model.addAttribute("guideList",
                boardService.getBoardsByJobAndType(
                        jobId,
                        BoardType.GUIDE,
                        PageRequest.of(0, 10)));

        model.addAttribute("macroList",
                boardService.getBoardsByJobAndType(
                        jobId,
                        BoardType.MACRO,
                        PageRequest.of(0, 10)));

        return "job/guide";
    }

    @GetMapping("/{jobId}/guide/register")
    public String writeGuide(
            @PathVariable Long jobId,
            Model model) {

        model.addAttribute("job",
                jobService.getJob(jobId));

        model.addAttribute("boardDTO",
                new BoardDTO());

        return "board/register";
    }
}
