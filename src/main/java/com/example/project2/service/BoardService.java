package com.example.project2.service;

import com.example.project2.repository.BoardLikeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project2.common.BoardType;
import com.example.project2.dto.AttachDTO;
import com.example.project2.dto.BoardDTO;
import com.example.project2.entity.Account;
import com.example.project2.entity.Attach;
import com.example.project2.entity.Board;
import com.example.project2.entity.Job;
import com.example.project2.repository.AccountRepository;
import com.example.project2.repository.AttachRepository;
import com.example.project2.repository.BoardFavoriteRepository;
import com.example.project2.repository.BoardRepository;
import com.example.project2.repository.CommentRepository;
import com.example.project2.repository.JobRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

        private final BoardLikeRepository boardLikeRepository;
        private final BoardRepository boardRepository;
        private final CommentRepository commentRepository;
        private final JobRepository jobRepository;
        private final AccountRepository accountRepository;
        private final AttachRepository attachRepository;
        private final FileStorageService fileStorageService;
        private final BoardLikeService boardLikeService;
        private final BoardFavoriteRepository boardFavoriteRepository;

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
        public int saveBoard(BoardDTO boardDTO, String username) {

                Account account = accountRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

                Job job = jobRepository.findById(boardDTO.getJobId())
                                .orElseThrow(() -> new IllegalArgumentException("직업이 존재하지 않습니다."));

                Board board = Board.builder()
                                .title(boardDTO.getTitle())
                                .content(boardDTO.getContent())
                                .boardType(boardDTO.getBoardType())
                                .account(account)
                                .job(job)
                                .build();

                List<Attach> attachs = boardDTO.getAttachList().stream()
                                .map(dto -> Attach.builder()
                                                .filePath(dto.getFilePath())
                                                .fileRealName(dto.getFileRealName())
                                                .fileChgName(dto.getFileChgName())
                                                .board(board)
                                                .build())
                                .toList();

                board.getAttachList().addAll(attachs);
                board.setAttachCount(attachs.size());

                boardRepository.save(board);

                return 1;
        }

        // 게시글 상세 조회
        @Transactional(readOnly = true)
        public BoardDTO getBoardById(Long boardId) {

                Board board = boardRepository.findByIdWithAttach(boardId);

                List<AttachDTO> attachDTOList = board.getAttachList() == null
                                ? new ArrayList<>()
                                : board.getAttachList().stream()
                                                .map(attach -> AttachDTO.builder()
                                                                .attachId(attach.getAttachId())
                                                                .filePath(attach.getFilePath())
                                                                .fileRealName(attach.getFileRealName())
                                                                .fileChgName(attach.getFileChgName())
                                                                .build())
                                                .toList();

                return BoardDTO.builder()
                                .boardId(board.getBoardId())
                                .jobId(board.getJob().getJobId())
                                .title(board.getTitle())
                                .content(board.getContent())
                                .nickname(board.getAccount().getNickname())
                                .username(board.getAccount().getUsername())
                                .viewCount(board.getViewCount())
                                .likeCount(board.getLikeCount())
                                .createdAt(board.getCreatedAt())
                                .attachList(attachDTOList)
                                .build();
        }

        public BoardDTO getBoardDetail(Long boardId, String username) {

                Board board = boardRepository.findByIdWithAttach(boardId);

                List<AttachDTO> attachDTOList = board.getAttachList() == null
                                ? new ArrayList<>()
                                : board.getAttachList().stream()
                                                .map(attach -> AttachDTO.builder()
                                                                .attachId(attach.getAttachId())
                                                                .filePath(attach.getFilePath())
                                                                .fileRealName(attach.getFileRealName())
                                                                .fileChgName(attach.getFileChgName())
                                                                .build())
                                                .toList();

                return BoardDTO.builder()
                                .boardId(board.getBoardId())
                                .jobId(board.getJob().getJobId())
                                .title(board.getTitle())
                                .content(board.getContent())
                                .nickname(board.getAccount().getNickname())
                                .username(board.getAccount().getUsername())
                                .viewCount(board.getViewCount())
                                .likeCount(board.getLikeCount())

                                .liked(
                                                username != null &&
                                                                boardLikeRepository
                                                                                .existsByBoardBoardIdAndAccountUsername(
                                                                                                boardId,
                                                                                                username))

                                .favorited(
                                                username != null &&
                                                                boardFavoriteRepository
                                                                                .existsByBoardBoardIdAndAccountUsername(
                                                                                                boardId,
                                                                                                username))

                                .createdAt(board.getCreatedAt())
                                .attachList(attachDTOList)
                                .build();
        }

        @Transactional
        public void increaseViewCount(Long boardId) {

                boardRepository.increaseViewCount(boardId);

        }

        @Transactional
        public BoardDTO getBoardById(Long boardId,
                        String username, // 💡 1. 컨트롤러에서 보낸 username을 받습니다.
                        HttpSession session) {

                String key = "viewed_" + boardId;

                Long lastView = (Long) session.getAttribute(key);

                long now = System.currentTimeMillis();

                // 처음 방문이거나 30분이 지난 경우만 조회수 증가
                if (lastView == null || now - lastView > 30 * 60 * 1000) {

                        increaseViewCount(boardId);

                        session.setAttribute(key, now);
                }

                Board board = boardRepository.findByIdWithAttach(boardId);
                int totalLikeCount = boardLikeService.getLikeCount(boardId);

                // 💡 [여기서 변수가 정상적으로 정의됩니다]
                List<AttachDTO> attachDTOList = board.getAttachList() == null
                                ? new ArrayList<>()
                                : board.getAttachList().stream()
                                                .map(attach -> AttachDTO.builder()
                                                                .attachId(attach.getAttachId())
                                                                .filePath(attach.getFilePath())
                                                                .fileRealName(attach.getFileRealName())
                                                                .fileChgName(attach.getFileChgName())
                                                                .build())
                                                .toList();

                return BoardDTO.builder()
                                .boardId(board.getBoardId())
                                .jobId(board.getJob().getJobId())
                                .title(board.getTitle())
                                .content(board.getContent())
                                .nickname(board.getAccount().getNickname())
                                .username(board.getAccount().getUsername()) // 유저네임 누락 방지
                                .viewCount(board.getViewCount())
                                .likeCount(totalLikeCount)

                                // 🚨 2. 새로고침 시 반영될 추천 여부(liked) 상태값 저장!
                                .liked(username != null && boardLikeRepository
                                                .existsByBoardBoardIdAndAccountUsername(boardId, username))

                                .createdAt(board.getCreatedAt())
                                .attachList(attachDTOList)
                                .build();
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
        public int updateBoard(Long boardId, BoardDTO dto) {

                Board board = boardRepository.findById(boardId)
                                .orElseThrow();

                board.setTitle(dto.getTitle());
                board.setContent(dto.getContent());

                // 1. 삭제 처리
                if (dto.getDeletedImageIds() != null) {
                        attachRepository.deleteAllById(dto.getDeletedImageIds());
                }

                // 2. 신규 파일 저장
                if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {

                        List<Attach> newAttachs = dto.getFiles().stream()
                                        // 🎯 [여기 추가] 빈 파일 스트림은 필터링하여 통과시키지 않음
                                        .filter(file -> file != null && !file.isEmpty()
                                                        && file.getOriginalFilename() != null
                                                        && !file.getOriginalFilename().isBlank())
                                        .map(file -> {
                                                String savedName = fileStorageService.save(file);

                                                return Attach.builder()
                                                                .fileRealName(file.getOriginalFilename())
                                                                .fileChgName(savedName)
                                                                .filePath("/upload/" + savedName)
                                                                .board(board)
                                                                .build();
                                        })
                                        .toList();

                        // 🎯 필터링을 거친 진짜 새 파일이 있을 때만 연관관계에 추가
                        if (!newAttachs.isEmpty()) {
                                board.getAttachList().addAll(newAttachs);
                        }
                }

                // 3. 삭제된 개수 등이 실시간 반영된 영속성 컨텍스트 기준으로 파일 수 카운트 조정
                board.setAttachCount(board.getAttachList().size());

                boardRepository.save(board);

                return 1;
        }

        // 게시글 삭제
        @Transactional
        public void deleteBoard(Long boardId) {
                // 즐겨찾기 삭제
                boardFavoriteRepository.deleteByBoardBoardId(boardId);

                // 추천 삭제
                boardLikeRepository.deleteByBoardBoardId(boardId);

                // 댓글 삭제
                commentRepository.deleteByBoardBoardId(boardId);

                // 마지막으로 게시글 삭제
                boardRepository.deleteById(boardId);
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

                int totalLikeCount = boardLikeService.getLikeCount(board.getBoardId());

                return BoardDTO.builder()
                                .boardId(board.getBoardId())
                                .jobId(board.getJob().getJobId())
                                .title(board.getTitle())
                                .nickname(board.getAccount().getNickname())
                                .content(board.getContent())
                                .viewCount(board.getViewCount())
                                .likeCount(totalLikeCount)
                                .createdAt(board.getCreatedAt())
                                .attachCount(board.getAttachCount())
                                .attachList(attachDTOs)
                                .build();
        }
}
