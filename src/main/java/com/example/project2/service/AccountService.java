package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project2.common.AccountRole;
import com.example.project2.dto.AccountDTO;
import com.example.project2.dto.BoardDTO;
import com.example.project2.dto.BoardFavoriteDTO;
import com.example.project2.dto.ReviewDTO;
import com.example.project2.entity.Account;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.BoardRepository;
import com.example.project2.repository.ReviewRepository;
import com.example.project2.repository.BoardFavoriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 🎯 롬복 @RequiredArgsConstructor를 통해 자동으로 의존성 주입을 받도록 final 지정
    private final BoardRepository boardRepository;
    private final ReviewRepository reviewRepository;
    private final BoardFavoriteRepository boardFavoriteRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole().name().replace("ROLE_", ""))
                .build();
    }

    // 관리자 계정 생성 메소드
    public void createAdmin() {
        if (accountRepository.findByUsername("admin").isPresent()) {
            return;
        }
        Account admin = Account.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .nickname("관리자")
                .role(AccountRole.ROLE_ADMIN)
                .build();
        accountRepository.save(admin);
    }

    public int register(AccountDTO accountDTO) {
        Account account = Account.builder()
                .username(accountDTO.getUsername())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .nickname(accountDTO.getNickname())
                .role(AccountRole.ROLE_USER)
                .build();
        accountRepository.save(account);
        return 1;
    }

    public AccountDTO findByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElse(null);

        if (account == null)
            return null;

        AccountDTO dto = new AccountDTO();
        dto.setUsername(account.getUsername());
        dto.setNickname(account.getNickname());

        return dto;
    }

    // 닉네임 중복 여부 확인
    public boolean isNicknameExists(String nickname) {
        return accountRepository.findByNickname(nickname).isPresent();
    }

    // 닉네임 업데이트
    @org.springframework.transaction.annotation.Transactional
    public void updateNickname(String username, String newNickname) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        account.setNickname(newNickname);
        accountRepository.save(account);
    }

    // 비밀번호 검증 및 업데이트
    @org.springframework.transaction.annotation.Transactional
    public boolean updatePassword(String username, String currentPassword, String newPassword) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            return false;
        }

        if (passwordEncoder.matches(newPassword, account.getPassword())) {
            return false;
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return true;
    }

    public List<BoardDTO> findPostsByUsername(String username) {
        return boardRepository.findByAccountUsername(username)
                .stream()
                .map(board -> {
                    BoardDTO dto = new BoardDTO();
                    dto.setBoardId(board.getBoardId());
                    dto.setTitle(board.getTitle());
                    dto.setViewCount(board.getViewCount());
                    dto.setLikeCount(board.getLikeCount());
                    dto.setCreatedAt(board.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> findReviewsByUsername(String username) {
        return reviewRepository.findByAccountUsername(username)
                .stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setReviewId(review.getReviewId());
                    dto.setContent(review.getContent());
                    dto.setScore(review.getScore());
                    dto.setCreatedAt(review.getCreatedAt());
                    if (review.getJob() != null)
                        dto.setJobName(review.getJob().getJobName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<BoardFavoriteDTO> findFavoritesByUsername(String username) {
        return boardFavoriteRepository.findByAccountUsername(username)
                .stream()
                .map(fav -> {
                    BoardFavoriteDTO dto = new BoardFavoriteDTO();
                    // 구조에 맞게 수동 세팅
                    if (fav.getBoard() != null) {
                        dto.setBoardId(fav.getBoard().getBoardId());
                        dto.setTitle(fav.getBoard().getTitle());
                        dto.setCreatedAt(fav.getBoard().getCreatedAt());
                        if (fav.getBoard().getAccount() != null)
                            dto.setNickname(fav.getBoard().getAccount().getNickname());
                        if (fav.getBoard().getJob() != null)
                            dto.setJobName(fav.getBoard().getJob().getJobName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}