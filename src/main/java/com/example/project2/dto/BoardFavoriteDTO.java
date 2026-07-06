package com.example.project2.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFavoriteDTO {

    private Long favoriteId;

    private Long boardId;

    private Long accountId;

    private LocalDateTime createdAt;

}