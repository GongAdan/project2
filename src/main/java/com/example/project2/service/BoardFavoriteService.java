package com.example.project2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.entity.Account;
import com.example.project2.entity.Board;
import com.example.project2.entity.BoardFavorite;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.BoardFavoriteRepository;
import com.example.project2.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardFavoriteService {

    private final BoardFavoriteRepository favoriteRepository;
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;

    // 즐겨찾기 등록/해제
    public boolean favorite(Long boardId,String username){

        if(favoriteRepository.existsByBoardBoardIdAndAccountUsername(boardId, username)){

            favoriteRepository.deleteByBoardBoardIdAndAccountUsername(boardId, username);

            return false;
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow();

        BoardFavorite favorite = BoardFavorite.builder()
                .board(board)
                .account(account)
                .build();

        favoriteRepository.save(favorite);

        return true;
    }
    

    // 즐겨찾기 여부

    public boolean isFavorited(Long boardId,String username){

        return favoriteRepository
                .existsByBoardBoardIdAndAccountUsername(boardId, username);
    }

    // 내 즐겨찾기 목록

    public List<BoardFavorite> getMyFavorites(String username){

        return favoriteRepository.findByAccountUsername(username);
    }

}