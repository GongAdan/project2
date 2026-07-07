package com.example.project2.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project2.common.JobRole;
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

    // 1. 메인 페이지 초기 로딩 및 비동기 갱신 시 모두 공유하는 데이터 세팅 메서드
    private void populateSignalModel(HttpSession session, Model model) {
        Long activeSignalId = (Long) session.getAttribute("signalId");
        boolean isSignalOn = (activeSignalId != null);

        model.addAttribute("isSignalOn", isSignalOn);
        model.addAttribute("allJobs", jobService.findAll());

        // 💡 DB에서 active = 1인 모든 참가자 목록을 안전하게 가져옵니다.
        List<Signal> activeSignals = signalService.findActiveSignals();
        model.addAttribute("activeSignals", activeSignals);
        model.addAttribute("queueCount", activeSignals != null ? activeSignals.size() : 0);

        if (isSignalOn) {
            Long jobId = (Long) session.getAttribute("jobId");
            if (jobId != null) {
                model.addAttribute("activeJob", jobService.getJob(jobId));
            }
        }
    }

    @GetMapping("/match")
    public String signalPage(HttpSession session, Model model) {
        model.addAttribute("tanks", jobService.findByRole(JobRole.valueOf("TANK")));
        model.addAttribute("healers", jobService.findByRole(JobRole.valueOf("HEALER")));
        model.addAttribute("melees", jobService.findByRole(JobRole.valueOf("MELEE")));
        model.addAttribute("rangeds", jobService.findByRole(JobRole.valueOf("RANGED")));
        model.addAttribute("casters", jobService.findByRole(JobRole.valueOf("CASTER")));

        populateSignalModel(session, model);
        return "main/index"; // 본인의 메인 뷰 이름으로 유지해주세요
    }

    // 2. 비동기 화면 조각(Fragment)만 요청받는 곳
    @GetMapping("/fragment")
    public String getSignalFragment(HttpSession session, Model model) {
        populateSignalModel(session, model);
        return "main/index :: signalContent";
    }

    // 3. 참가 신청 (원래대로 redirect 처리하여 DB 저장 안정성 확보)
    @PostMapping("/on")
    public String on(@RequestParam Long jobId, HttpSession session) {
        signalService.activate(jobId, session);
        session.setAttribute("jobId", jobId);
        return "redirect:/signals/fragment"; // 💡 저장 후 fragment 경로로 리다이렉트!
    }

    // 4. 참가 취소
    @PostMapping("/off")
    public String off(HttpSession session) {
        signalService.deactivate(session);
        session.removeAttribute("jobId");
        return "redirect:/signals/fragment"; // 💡 삭제 후 fragment 경로로 리다이렉트!
    }

    @GetMapping("/jobs-fragment")
    public String getJobsFragment(Model model) {
        // 기존 메인 페이지(/match) 로딩할 때 사용하시던 직업 데이터 조회 로직을 그대로 넣어줍니다.
        model.addAttribute("tanks", jobService.findByRole(JobRole.valueOf("TANK")));
        model.addAttribute("healers", jobService.findByRole(JobRole.valueOf("HEALER")));
        model.addAttribute("melees", jobService.findByRole(JobRole.valueOf("MELEE")));
        model.addAttribute("rangeds", jobService.findByRole(JobRole.valueOf("RANGED")));
        model.addAttribute("casters", jobService.findByRole(JobRole.valueOf("CASTER")));

        // index.html 안에 선언된 th:fragment="jobListContent" 영역만 쏙 잘라서 리턴합니다.
        return "main/index :: jobListContent"; // 본인의 실제 폴더 구조(예: main/index)에 맞게 수정해 주세요.
    }
}