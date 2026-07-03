package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.dto.MacroDTO;
import com.example.project2.entity.Macro;
import com.example.project2.repository.MacroRepository;

import lombok.RequiredArgsConstructor;

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
                        macro.getContent()
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
                .build();

        macroRepository.save(macro);

        // 저장 후 해당 직업의 최신 리스트를 바로 반환하여 비동기 화면 갱신에 사용
        return findByJobIdOrderByMacroIdAsc(macroDTO.getJobId());
    }
}