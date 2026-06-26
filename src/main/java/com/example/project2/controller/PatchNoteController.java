package com.example.project2.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.project2.dto.PatchNoteDTO;
import com.example.project2.service.PatchNoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/patchNote")
@RequiredArgsConstructor
public class PatchNoteController {

    private final PatchNoteService patchNoteService;

    // 패치노트 목록
    @GetMapping
    public String list(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model) {

        Page<PatchNoteDTO> patchNotes = patchNoteService.getPatchNotes(pageable);

        model.addAttribute("patchNotes", patchNotes);

        return "patchnote/list";
    }

    // 패치노트 상세
    @GetMapping("/{patchNoteId}")
    public String detail(
            @PathVariable Long patchNoteId,
            Model model) {

        PatchNoteDTO patchNote = patchNoteService.getPatchNote(patchNoteId);

        model.addAttribute("patchNote", patchNote);

        return "patchnote/detail";
    }

    // 등록 페이지
    @GetMapping("/register")
    public String registerForm() {
        return "patchnote/register";
    }

    // 등록 처리
    @PostMapping("/register")
    public String register(
            @ModelAttribute PatchNoteDTO patchNoteDTO) {

        patchNoteService.savePatchNote(patchNoteDTO);

        return "redirect:patchNote/list";
    }

    // 수정 페이지
    @GetMapping("/{patchNoteId}/edit")
    public String editForm(
            @PathVariable Long patchNoteId,
            Model model) {

        model.addAttribute("patchNote",
                patchNoteService.getPatchNote(patchNoteId));

        return "patchnote/edit";
    }

    // 수정 처리
    @PostMapping("/{patchNoteId}/edit")
    public String edit(
            @PathVariable Long patchNoteId,
            @ModelAttribute PatchNoteDTO patchNoteDTO) {

        patchNoteService.updatePatchNote(patchNoteId, patchNoteDTO);

        return "redirect:patchNote/list/" + patchNoteId;
    }

    // 삭제
    @PostMapping("/{patchNoteId}/delete")
    public String delete(
            @PathVariable Long patchNoteId) {

        patchNoteService.deletePatchNote(patchNoteId);

        return "redirect:patchNote/list";
    }

}