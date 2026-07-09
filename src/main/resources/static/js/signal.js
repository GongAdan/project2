// signal.js
function refreshFragment() {
    // window.currentTab을 통해 main.js에서 설정한 값 확인
    if (currentTab !== 'signal') return;

    const tierSelect = document.getElementById('tierSelect');
    const selectedTier = tierSelect ? tierSelect.value : 'BRONZE';

    fetch('/signals/fragment?tier=' + selectedTier)
        .then(res => res.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
        });
}

function toggleSignal(action) {
    var url = action === 'on' ? '/signals/on' : '/signals/off';
    var params = new URLSearchParams();
    var tierSelect = document.getElementById('tierSelect');
    params.append('tier', tierSelect.value);

    if (action === 'on') {
        var jobId = document.getElementById('jobSelect').value;
        if (!jobId) { alert('직업을 선택해 주세요.'); return; }
        params.append('jobId', jobId);
    }

    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    }).then(() => refreshFragment());
}

// 10초마다 자동 새로고침 (signal 탭일 때만)
setInterval(() => {
    if (currentTab === 'signal') refreshFragment();
}, 10000);

// static/js/signal.js

function updateTierPreview(selectElement) {
    var selectedTier = selectElement.value;
    var tierIcon = document.getElementById('selected-tier-icon');
    
    // 1. 좌측의 티어 미리보기 아이콘 즉시 교체
    if (tierIcon) {
        tierIcon.src = '/images/tiers/' + selectedTier + '.png';
    }
    
    // 2. 선택한 티어의 매칭 현황을 새로고침하기 위해 프래그먼트 호출
    if (typeof refreshFragment === 'function') {
        refreshFragment();
    }
}

// 기존 refreshFragment 함수 내부 보완
function refreshFragment() {
    if (currentTab !== 'signal') return;

    const tierSelect = document.getElementById('tierSelect');
    const selectedTier = tierSelect ? tierSelect.value : 'BRONZE';

    fetch('/signals/fragment?tier=' + selectedTier)
        .then(res => res.text())
        .then(html => {
            document.getElementById('dynamic-content').innerHTML = html;
            
            // 데이터 로드 완료 후 바뀐 상태 스냅샷에 맞춰서 한 번 더 로컬 이미지 주소 보정
            const newSelect = document.getElementById('tierSelect');
            const newIcon = document.getElementById('selected-tier-icon');
            if(newSelect && newIcon) {
                newIcon.src = '/images/tiers/' + newSelect.value + '.png';
            }
        });
}