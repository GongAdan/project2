// main.js
let currentTab = 'guide'; // 전역 변수

document.addEventListener('DOMContentLoaded', () => {
    // 페이지 로드 시 기본 가이드 화면 로드
    document.getElementById('job-list-btn').click();
});

// 메뉴 이벤트 리스너 통합
document.addEventListener('click', function (e) {
    if (e.target && (e.target.id === 'signal-btn' || e.target.closest('#signal-btn'))) {
        e.preventDefault();
        currentTab = 'signal';
        // signal.js에 정의된 함수 호출
        if (typeof refreshFragment === 'function') refreshFragment();
    }

    if (e.target && (e.target.id === 'job-list-btn' || e.target.closest('#job-list-btn'))) {
        e.preventDefault();
        currentTab = 'guide';
        loadJobList(); // job-list.js에 정의된 함수
    }
});

function loadJobList() {
    // 이제 /job/ 경로로 요청을 보냅니다.
    fetch('/job/jobs-fragment')
        .then(res => res.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
        })
        .catch(err => console.error('직업 목록 로드 실패:', err));
}