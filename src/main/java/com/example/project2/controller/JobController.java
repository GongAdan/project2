package com.example.project2.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project2.common.BoardType;
import com.example.project2.dto.BoardDTO;
import com.example.project2.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/main/jobs")
@RequiredArgsConstructor
public class JobController {

    private final BoardService boardService;

    @GetMapping("/{jobId}/boards")
    public String boardList(
            @PathVariable Long jobId,
            @RequestParam BoardType type,
            @PageableDefault(
                size = 10,
                sort = "boardId",
                direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model) {

        Page<BoardDTO> boards = boardService.getBoardsByJobAndType(
                jobId,
                type,
                pageable);

        model.addAttribute("boards", boards);
        model.addAttribute("jobId", jobId);
        model.addAttribute("type", type);

        return "board/list";
    }
}
