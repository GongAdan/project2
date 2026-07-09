package com.example.project2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project2.dto.AccountDTO;
import com.example.project2.dto.BoardDTO;
import com.example.project2.dto.BoardFavoriteDTO;
import com.example.project2.dto.ReviewDTO;
import com.example.project2.service.AccountService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public void loginForm(String errorMsg, String logoutMsg, Model model) {
        if (errorMsg != null) {
            model.addAttribute("errorMsg", "아이디 비밀번호 확인");
        }
        if (logoutMsg != null) {
            model.addAttribute("logoutMsg", "로그아웃되었습니다.");
        }
    }

    @GetMapping("/myPage")
    public String myPage(Authentication authentication, Model model) {

        if (authentication != null) {
            String username = authentication.getName();
            model.addAttribute("username", username);

            // 🎯 서비스에서 회원 규격을 AccountDTO로 받아옵니다.
            AccountDTO accountDTO = accountService.findByUsername(username);
            if (accountDTO != null) {
                model.addAttribute("nickname", accountDTO.getNickname());
            } else {
                model.addAttribute("nickname", "");
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("nickname", "");
        }

        return "auth/myPage";
    }

    // @ResponseBody가 붙어야 HTML이 아닌 JSON 데이터가 브라우저로 날아갑니다.
    @GetMapping("/myPage/activity")
    @ResponseBody
    public ResponseEntity<?> getMyActivity(
            @RequestParam("type") String type,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String username = authentication.getName();

        // 탭 종류(posts, reviews, favorites)에 따라 서비스 호출 후 응답
        switch (type) {
            case "posts":
                // 💡 accountService 혹은 관련 서비스에 구현된 게시글 목록 가져오기 호출
                List<BoardDTO> myPosts = accountService.findPostsByUsername(username);
                return ResponseEntity.ok(myPosts);

            case "reviews":
                // 💡 본인이 작성한 리뷰 목록 가져오기 호출
                List<ReviewDTO> myReviews = accountService.findReviewsByUsername(username);
                return ResponseEntity.ok(myReviews);

            case "favorites":
                // 💡 본인의 즐겨찾기 목록 가져오기 호출
                List<BoardFavoriteDTO> myFavorites = accountService.findFavoritesByUsername(username);
                return ResponseEntity.ok(myFavorites);

            default:
                return ResponseEntity.badRequest().body("잘못된 요청 사양입니다.");
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/myPage/update-nickname")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> updateNickname(
            Authentication authentication,
            @org.springframework.web.bind.annotation.RequestBody com.example.project2.dto.AccountDTO dto) {

        if (authentication == null) {
            return org.springframework.http.ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String username = authentication.getName();

        // 닉네임 중복 체크 한 번 더 수행
        if (accountService.isNicknameExists(dto.getNickname())) {
            return org.springframework.http.ResponseEntity.badRequest().body("이미 사용 중인 닉네임입니다.");
        }

        accountService.updateNickname(username, dto.getNickname());
        return org.springframework.http.ResponseEntity.ok("닉네임이 변경되었습니다.");
    }

    @org.springframework.web.bind.annotation.PutMapping("/myPage/update-password")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> updatePassword(
            Authentication authentication,
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> params) {

        if (authentication == null) {
            return org.springframework.http.ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String username = authentication.getName();
        String currentPassword = params.get("currentPassword");
        String newPassword = params.get("newPassword");

        // AccountService에서 matches()를 이용해 진짜 1234인지 검증하고 새 암호로 바꿉니다.
        boolean success = accountService.updatePassword(username, currentPassword, newPassword);
        if (!success) {
            return org.springframework.http.ResponseEntity.badRequest()
                    .body("기존 비밀번호가 일치하지 않거나, 새 비밀번호가 기존 비밀번호와 동일합니다.");
        }

        return org.springframework.http.ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @org.springframework.web.bind.annotation.PostMapping("/withdraw")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<String> withdraw(
            Authentication authentication,
            jakarta.servlet.http.HttpServletRequest request) { // 👈 세션 제어를 위해 request 추가

        if (authentication == null) {
            return org.springframework.http.ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String username = authentication.getName();

        try {
            // 1. 서비스 단에 회원 탈퇴 로직 실행 (마스킹)
            accountService.withdrawUser(username);

            // 2. 🎯 [세션 파기] 서버에 저장된 현재 유저의 로그인 세션 무효화
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // 세션 날리기
            }

            // 3. 🎯 [시큐리티 컨텍스트 초기화] 스프링 시큐리티 인증 정보 지우기
            org.springframework.security.core.context.SecurityContextHolder.clearContext();

            return org.springframework.http.ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");

        } catch (Exception e) {
            // 🎯 [여기 추가] 백엔드 콘솔창에 진짜 에러가 왜 났는지 빨간 글씨로 출력하라는 명령어입니다.
            e.printStackTrace();

            return org.springframework.http.ResponseEntity.status(500).body("회원 탈퇴 처리 중 내부 오류가 발생했습니다.");
        }

    }
}