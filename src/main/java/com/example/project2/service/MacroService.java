package com.example.project2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.dto.MacroDTO;
import com.example.project2.entity.Macro;
import com.example.project2.repository.MacroRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MacroService {

    private final MacroRepository macroRepository;

    // 1. 특정 직업의 매크로 목록 조회
    public List<MacroDTO> findByJobIdOrderByMacroIdAsc(Long jobId) {
        List<Macro> macros = macroRepository.findByJobId(jobId);
        
        // Entity -> DTO 변환 
        return macros.stream()
                .map(macro -> new MacroDTO(
                        macro.getMacroId(),
                        macro.getJobId(),
                        macro.getTitle(),
                        macro.getContent(),
                        macro.getUsername() 
                ))
                .collect(Collectors.toList());
    }

    // 2. 새 매크로 저장 후 최신 목록 반환
    @Transactional
    public List<MacroDTO> saveMacro(MacroDTO macroDTO) {
        Macro macro = Macro.builder()
                .jobId(macroDTO.getJobId())
                .title(macroDTO.getTitle())
                .content(macroDTO.getContent())
                .username(macroDTO.getUsername()) 
                .build();

        macroRepository.save(macro);

        // 저장 후 해당 직업의 최신 리스트를 바로 반환하여 비동기 화면 갱신에 사용
        return findByJobIdOrderByMacroIdAsc(macroDTO.getJobId());
    }

    // 🎯 3. 매크로 수정 기능 추가
    @Transactional
    public List<MacroDTO> updateMacro(Long macroId, MacroDTO macroDTO, String currentUsername) {
        Macro macro = macroRepository.findById(macroId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매크로가 존재하지 않습니다. ID: " + macroId));

        // 🔒 본인 확인 검증
        if (macro.getUsername() == null || !macro.getUsername().equals(currentUsername)) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }

        // 기존 스타일대로 새 객체를 빌드하여 수정(덮어쓰기) 진행
        Macro updatedMacro = Macro.builder()
                .macroId(macro.getMacroId()) // 기존 ID 유지
                .jobId(macro.getJobId())     // 기존 jobId 유지
                .title(macroDTO.getTitle())  // 받아온 데이터로 변경
                .content(macroDTO.getContent()) // 받아온 데이터로 변경
                .username(macro.getUsername()) // 작성자는 원래 작성자로 고정
                .build();

        macroRepository.save(updatedMacro);

        // 수정 완료 후 최신 리스트 반환
        return findByJobIdOrderByMacroIdAsc(macro.getJobId());
    }

    // 🎯 4. 매크로 삭제 기능 추가
    @Transactional
    public List<MacroDTO> deleteMacro(Long macroId, String currentUsername) {
        Macro macro = macroRepository.findById(macroId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매크로가 존재하지 않습니다. ID: " + macroId));

        // 🔒 본인 확인 검증
        if (macro.getUsername() == null || !macro.getUsername().equals(currentUsername)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        Long jobId = macro.getJobId(); // 리스트 조회용으로 보관
        macroRepository.delete(macro);

        // 삭제 완료 후 최신 리스트 반환
        return findByJobIdOrderByMacroIdAsc(jobId);
    }
}