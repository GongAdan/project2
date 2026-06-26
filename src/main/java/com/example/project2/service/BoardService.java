package com.example.project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.project2.common.BoardType;
import com.example.project2.dto.AttachDTO;
import com.example.project2.dto.BoardDTO;
import com.example.project2.entity.Attach;
import com.example.project2.entity.Board;
import com.example.project2.repository.BoardRepository;
import com.example.project2.repository.CommentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

        private final BoardRepository boardRepository;
        private final CommentRepository commentRepository;

        public Page<BoardDTO> getBoardsByJobAndType(
                        Long jobId,
                        BoardType type,
                        Pageable pageable) {

                Page<Board> boards = boardRepository.findByJobJobIdAndBoardType(
                                jobId,
                                type,
                                pageable);

                return boards.map(this::toDTO);
        }

        // 게시글 등록
        @Transactional
        public int saveBoard(BoardDTO boardDTO) {
                // board 인스턴스 생성(title, content, writer 데이터 적용)
                // boardDTO에서 게시판 데이터만 board로 변경
                Board board = Board.builder()
                                .title(boardDTO.getTitle())
                                .content(boardDTO.getContent())
                                .boardType(boardDTO.getBoardType())
                                .build();

                // boardDTO에서 attachDTO데이터만 attachs로 변경
                List<Attach> attachs = boardDTO.getAttachList().stream()
                                .map(attachDTO -> Attach.builder()
                                                .filePath(attachDTO.getFilePath())
                                                .fileRealName(attachDTO.getFileRealName())
                                                .fileChgName(attachDTO.getFileChgName())
                                                .board(board) // boardId 매칭을 위한 적용
                                                .build())
                                .collect(Collectors.toList());

                // board 엔티티에 attachs 적용
                board.getAttachList().addAll(attachs);
                // board 엔티티에 attachs.size 적용
                board.setAttachCount(attachs.size());

                // 게시글 데이터 JPA를 이용하여 DB에 저장
                // board에 attachs 엔티티가 적용되어 있으므로
                // board 테이블 insert와 attach 테이블의 insert가
                // 한 번에 실행됨(JPA 연결 특성으로 인해)
                boardRepository.save(board);

                return 1;
        }

        // 게시글 상세 조회
        public BoardDTO getBoardById(Long boardId) {
                // Optional<T> findById(ID id) :
                // - T : JpaRepository<T, id>에서 T에 설정된 엔티티(Board)
                // - id : JpaRepository<T, id>에서 id에 설정된 PK(Long boardId)
                // Optional<Board> : 게시글이 존재하면 Optional안에 Board 객체가 존재
                // 존재하지 않으면 Optional.empty()
                // orElseThrow() : Optional안에 Board 객체가 없으면
                // throw new Exception - exception을 실행
                Board board = boardRepository.findById(boardId)
                                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

                return toDTO(board);
        }

        // 전체 게시글 조회(페이징 처리)
        public Page<BoardDTO> getBoards(Pageable pageable) {
                // Pageable : 스프링 Data JPA에서 제공하는 페이징 처리 객체
                // (페이지 번호, 크기, 정렬 방식 등 선택 가능)
                // Page<T> : T 엔티티 형태로 페이징 처리된 데이터를 리턴
                // (실제 데이터 목록, 전체 데이터 수, 전체 페이지 수 등)
                Page<Board> boards = boardRepository.findAll(pageable);
                // Page<Board> findAll(pageable) :
                // pageable의 페이지 번호, 크기, 정렬 방식 등을 매개변수로 받아와서
                // 페이징 처리 쿼리를 수행하는 메소드
                // 페이징 처리된 데이터는 List<Board> 형태와 동일

                // Page<Board> -> Page<BoardDTO> 수행
                // Page<Board> -> List<Board> 꺼냄
                // -> List<BoardDTO> 변환 -> Page<BoardDTO>로 구성
                // List.map(method()) : 리스트의 각 데이터에 접근하여
                // 메소드 구조에 맞게 데이터를 매핑하는 역할
                return boards.map(this::toDTO);
        }

        // 게시글 수정
        public int updateBoard(Long boardId, BoardDTO boardDTO) {
                // JPA에서 데이터 수정의 경우
                // 존재하는 데이터를 조회하고
                // 조회된 데이터의 특정 값을 변경한 후에
                // 다시 등록하는 방식(update가 없음)
                Board board = boardRepository.findById(boardId)
                                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
                board.setTitle(boardDTO.getTitle());
                board.setContent(boardDTO.getContent());

                // 그 게시글에 포함된 기존 첨부파일 모두 제거
                board.getAttachList().clear();
                board.setAttachCount(0);

                if (boardDTO.getAttachList() != null) {
                        List<Attach> attachs = boardDTO.getAttachList().stream()
                                        .map(attachDTO -> Attach.builder()
                                                        .filePath(attachDTO.getFilePath())
                                                        .fileRealName(attachDTO.getFileRealName())
                                                        .fileChgName(attachDTO.getFileChgName())
                                                        .board(board) // boardId 매칭을 위한 적용
                                                        .build())
                                        .collect(Collectors.toList());

                        // attach 테이블에 attach list 데이터 저장
                        board.getAttachList().addAll(attachs);
                        board.setAttachCount(attachs.size());
                }

                boardRepository.save(board);
                return 1;
        }

        // 게시글 삭제 시 댓글 삭제하는 방법
        // 1. deleteBoard() 메소드처럼 직접 두 개의 테이블에 접근하여 삭제
        // 2. comment 엔티티에 외래키로 boardId를 설정하고,
        // CASCADE를 설정하여 부모 테이블 데이터(board)가 삭제되면
        // 자식도 삭제되도록 구성
        @Transactional
        public void deleteBoard(Long boardId) {
                boardRepository.deleteById(boardId);
                commentRepository.deleteByBoardBoardId(boardId);
        }

        // Board -> BoardDTO 변경 메소드
        private BoardDTO toDTO(Board board) {
                List<AttachDTO> attachDTOs = board.getAttachList().stream()
                                .map(attach -> AttachDTO.builder()
                                                .attachId(attach.getAttachId())
                                                .filePath(attach.getFilePath())
                                                .fileRealName(attach.getFileRealName())
                                                .fileChgName(attach.getFileChgName())
                                                .build())
                                .collect(Collectors.toList());

                return BoardDTO.builder()
                                .boardId(board.getBoardId())
                                .title(board.getTitle())
                                .content(board.getContent())
                                .nickname(board.getAccount().getNickname())
                                .jobName(board.getJob().getJobName())
                                .commentCount(board.getComments().size())
                                .build();
        }
}
