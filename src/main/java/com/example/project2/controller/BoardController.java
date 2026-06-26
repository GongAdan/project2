package com.example.project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project2.dto.BoardDTO;
import com.example.project2.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    // BoardService 빈 객체 주입
    private final BoardService boardService;

    // 게시글 등록 페이지
    @GetMapping("/register")
    public void registerForm() {

    }

    // 게시글 등록 처리
    @PostMapping("/register")
    public String registerBoard(@ModelAttribute BoardDTO boardDTO) {
        // @ModelAttribute : form data, request parameter로 전송된
        // 데이터를 객체에 바인딩 해주는 역할

        boardService.saveBoard(boardDTO);
        return "redirect:/board/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/detail")
    public void detailBoard(Long boardId, Model model) {
        BoardDTO board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
    }

    // 게시글 수정 페이지
    @GetMapping("/edit")
    public void editForm(Long boardId, Model model) {
        BoardDTO board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
    }

    // 게시글 수정 처리
    @PostMapping("/edit")
    public String editBoard(Long boardId,
            BoardDTO boardDTO) {

        boardService.updateBoard(boardId, boardDTO);

        return "redirect:/board/detail?boardId=" + boardId;
    }

    // 게시글 삭제 처리
    @PostMapping("/delete")
    public String deleteBoard(Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/board/list";
    }
}
