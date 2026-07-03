package com.example.project2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project2.dto.BoardDTO;
import com.example.project2.dto.CommentDTO;
import com.example.project2.dto.JobDTO;
import com.example.project2.service.BoardService;
import com.example.project2.service.FileService;
import com.example.project2.service.JobService;
import com.example.project2.service.CommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    // BoardService 빈 객체 주입
    private final BoardService boardService;
    private final JobService jobService;
    private final FileService fileService;
    private final CommentService commentService;

    // 게시글 등록 페이지
    @GetMapping("/register")
    public String registerForm(@RequestParam Long jobId, Model model) {

        JobDTO job = jobService.getJob(jobId);

        model.addAttribute("job", job);

        return "board/register";
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
    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteBoard(@RequestParam Long boardId,
            Authentication authentication) { // 💡 시큐리티 Authentication 추가

        // 1. 현재 로그인한 사용자가 아예 없다면 권한 없음(403) 반환
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String currentUsername = authentication.getName();

        // 2. 게시글 상세 정보 조회 (기존에 작성해두신 메서드 활용)
        // 💡 단, BoardDTO에 작성자의 username(로그인 식별값)을 담아오는 필드가 있어야 합니다.
        BoardDTO board = boardService.getBoardDetail(boardId, currentUsername);

        // 3. 로그인한 유저와 게시글 작성자가 일치하는지 검증
        // (만약 BoardDTO에 저장된 필드명이 username이 아니라 다른 것이라면 board.getXXX()로 매치해주세요)
        if (!currentUsername.equals(board.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 작성자가 다르면 403 에러 반환
        }

        // 4. 검증 통과 시 삭제 처리
        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }

    // 파일 등록
    @PostMapping("/register")
    public String registerBoard(
            @ModelAttribute BoardDTO boardDTO,
            Authentication authentication) {

        boardDTO.setAttachList(
                fileService.upload(boardDTO.getFiles()));

        boardService.saveBoard(
                boardDTO,
                authentication.getName());

        return "redirect:/job/" + boardDTO.getJobId();
    }

    @GetMapping("/detail")
    public String boardDetail(@RequestParam Long boardId, Model model,
            Authentication authentication, HttpSession session) {

        String username = (authentication != null) ? authentication.getName() : null;

        BoardDTO boardDTO = boardService.getBoardById(boardId, username, session);
        model.addAttribute("board", boardDTO);

        List<CommentDTO> commentList = commentService.getCommentsByBoardId(boardId);
        model.addAttribute("commentList", commentList);

        return "board/detail";
    }
}
