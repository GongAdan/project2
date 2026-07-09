// job-list.js
function loadJobList() {
    // 이전: fetch('/signals/jobs-fragment')
    // 수정: JobController의 @RequestMapping("/job") + 메서드 매핑("/jobs-fragment")
    fetch('/job/jobs-fragment')
        .then(res => res.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
        })
        .catch(err => console.error('직업 목록 로드 실패:', err));
}