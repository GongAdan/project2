package com.example.project2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.project2.dto.PatchNoteDTO;
import com.example.project2.entity.Account;
import com.example.project2.entity.PatchNote;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.PatchNoteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatchNoteService {

    private final PatchNoteRepository patchNoteRepository;
    private final AccountRepository accountRepository;

    // 패치노트 등록
    @Transactional
    public void savePatchNote(PatchNoteDTO dto) {

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() ->
                        new IllegalArgumentException("관리자가 존재하지 않습니다."));

        PatchNote patchNote = PatchNote.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .account(account)
                .build();

        patchNoteRepository.save(patchNote);
    }

    // 전체 조회
    public Page<PatchNoteDTO> getPatchNotes(Pageable pageable) {

        return patchNoteRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // 상세 조회
    public PatchNoteDTO getPatchNote(Long patchNoteId) {

        PatchNote patchNote = patchNoteRepository.findById(patchNoteId)
                .orElseThrow(() ->
                        new IllegalArgumentException("패치노트가 존재하지 않습니다."));

        return toDTO(patchNote);
    }

    // 수정
    @Transactional
    public void updatePatchNote(Long patchNoteId,
                                PatchNoteDTO dto) {

        PatchNote patchNote = patchNoteRepository.findById(patchNoteId)
                .orElseThrow(() ->
                        new IllegalArgumentException("패치노트가 존재하지 않습니다."));

        patchNote.setTitle(dto.getTitle());
        patchNote.setContent(dto.getContent());
    }

    // 삭제
    @Transactional
    public void deletePatchNote(Long patchNoteId) {

        patchNoteRepository.deleteById(patchNoteId);
    }

    // Entity → DTO
    private PatchNoteDTO toDTO(PatchNote patchNote) {

        return PatchNoteDTO.builder()
                .patchNoteId(patchNote.getPatchNoteId())
                .title(patchNote.getTitle())
                .content(patchNote.getContent())
                .createdAt(patchNote.getCreatedAt())
                .updatedAt(patchNote.getUpdatedAt())
                .accountId(patchNote.getAccount().getAccountId())
                .nickname(patchNote.getAccount().getNickname())
                .build();
    }                       
}