package com.example.project2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.project2.service.BoardFavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardFavoriteController {

    private final BoardFavoriteService favoriteService;

    @PostMapping("/favorite")
    public Map<String,Object> favorite(
            @RequestParam Long boardId,
            Authentication authentication){

        Map<String,Object> result = new HashMap<>();

        if(authentication==null){

            result.put("login",false);

            return result;
        }

        boolean favorited =
                favoriteService.favorite(
                        boardId,
                        authentication.getName());

        result.put("login",true);
        result.put("favorited",favorited);

        return result;
    }
}