package com.example.project2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project2.dto.MacroDTO;
import com.example.project2.service.MacroService;

import java.security.Principal;
import java.util.List;

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

    // 🎯 [추가] 매크로 수정 (수정 완료 후 최신 리스트 반환)
    @PutMapping("/{macroId}")
    public ResponseEntity<?> updateMacro(@PathVariable Long macroId, 
                                         @RequestBody MacroDTO macroDTO, 
                                         Principal principal) {
        // 비로그인 상태 체크
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        try {
            List<MacroDTO> updatedList = macroService.updateMacro(macroId, macroDTO, principal.getName());
            return ResponseEntity.ok(updatedList);
        } catch (IllegalStateException e) {
            // 본인 글이 아닐 때 (403 Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // 매크로 ID를 찾을 수 없을 때 (404 Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 🎯 [추가] 매크로 삭제 (삭제 완료 후 최신 리스트 반환)
    @DeleteMapping("/{macroId}")
    public ResponseEntity<?> deleteMacro(@PathVariable Long macroId, 
                                         Principal principal) {
        // 비로그인 상태 체크
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        try {
            List<MacroDTO> updatedList = macroService.deleteMacro(macroId, principal.getName());
            return ResponseEntity.ok(updatedList);
        } catch (IllegalStateException e) {
            // 본인 글이 아닐 때 (403 Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // 매크로 ID를 찾을 수 없을 때 (404 Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}