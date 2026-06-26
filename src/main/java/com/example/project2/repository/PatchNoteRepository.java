package com.example.project2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.PatchNote;

public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {

Optional<PatchNote> findByAccountAccountId(Long accountId);
}
