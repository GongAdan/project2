package com.example.project2.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project2.common.Tier;
import com.example.project2.entity.Signal;
import com.example.project2.service.JobService;
import com.example.project2.service.SignalService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signals")
@RequiredArgsConstructor
public class SignalController {

    private final SignalService signalService;
    private final JobService jobService;

    // 💡 변경: 티어 정보를 파라미터로 받아 모델을 구성합니다.
    private void populateSignalModel(HttpSession session, Model model, Tier tier) {
        // 1. 로그인/비로그인 공통 데이터
        model.addAttribute("allJobs", jobService.findAll());

        // 2. 티어별 데이터 조회 (티어가 없으면 기본값 설정 가능)
        Tier targetTier = (tier != null) ? tier : Tier.BRONZE;

        List<Signal> activeSignals = signalService.findActiveSignalsByTier(targetTier);
        model.addAttribute("activeSignals", activeSignals);
        model.addAttribute("queueCount", activeSignals.size());

        // 3. 방이 꽉 찼는지 체크
        model.addAttribute("isFull", signalService.isRoomFull(targetTier));
        model.addAttribute("currentTier", targetTier);

        // 4. 본인 등록 여부 체크 (세션 ID 기준)
        boolean isSignalOn = signalService.isUserParticipating(session.getId());
        model.addAttribute("isSignalOn", isSignalOn);
    }

    @GetMapping("/match")
    public String signalPage(HttpSession session, Model model, @RequestParam(required = false) Tier tier) {
        // ... (tanks, healers 등 기존 jobService 코드 동일) ...
        populateSignalModel(session, model, tier);
        return "main/index";
    }

    // 💡 비동기 갱신 시 티어 파라미터를 넘겨받아야 합니다.
    @GetMapping("/fragment")
    public String getSignalFragment(HttpSession session, Model model, @RequestParam(required = false) Tier tier) {
        populateSignalModel(session, model, tier);
        // 수정: 이제 분리한 파일 경로를 리턴합니다.
        return "fragments/signal-view :: signalContent";
    }

    // 💡 변경: tier 파라미터를 추가로 받습니다.
    @PostMapping("/on")
    public String on(@RequestParam Long jobId, @RequestParam String tier, HttpSession session) {
        signalService.activate(jobId, tier, session);
        return "redirect:/signals/fragment?tier=" + tier;
    }

    // 💡 변경: 삭제 시에도 어떤 티어였는지 알아야 올바른 페이지로 리다이렉트 됩니다.
    @PostMapping("/off")
    public String off(@RequestParam String tier, HttpSession session) {
        signalService.deactivateBySessionId(session.getId());
        return "redirect:/signals/fragment?tier=" + tier;
    }

    // 창 닫기용 (세션 정리)
    @PostMapping("/close-session")
    @ResponseBody // HTML 리턴이 아님
    public void closeSession(HttpSession session) {
        signalService.deactivateBySessionId(session.getId());
        session.invalidate();
    }

}