package com.example.project2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project2.dto.MacroDTO;
import com.example.project2.service.MacroService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/macro")
@RequiredArgsConstructor
public class MacroRestController {

    private final MacroService macroService;

    // 특정 직업의 매크로 목록 조회
    @GetMapping("/list/{jobId}")
    public ResponseEntity<List<MacroDTO>> getMacros(@PathVariable Long jobId) {
        List<MacroDTO> list = macroService.findByJobIdOrderByMacroIdAsc(jobId);
        return ResponseEntity.ok(list);
    }

    // 새 매크로 메모 등록
    @PostMapping
    public ResponseEntity<List<MacroDTO>> createMacro(@RequestBody MacroDTO macroDTO) {
        List<MacroDTO> updatedList = macroService.saveMacro(macroDTO);
        return ResponseEntity.ok(updatedList);
    }
}
