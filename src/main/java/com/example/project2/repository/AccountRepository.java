package com.example.project2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project2.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findByNickname(String nickname);

    boolean existsByUsername(String username);

}
