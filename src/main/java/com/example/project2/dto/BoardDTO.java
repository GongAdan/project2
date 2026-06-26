package com.example.project2.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project2.common.BoardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private Long boardId;
    private String title;
    private String content;
    private BoardType boardType;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long accountId;
    private String nickname;
    private Long jobId;
    private String jobName;
    private Integer commentCount;
    @Builder.Default
    private List<AttachDTO> attachList = new ArrayList<>();

}