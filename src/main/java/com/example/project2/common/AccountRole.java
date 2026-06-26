package com.example.project2.common;

import lombok.Getter;

// enum 타입 : 정해진 값들을 하나의 세트로 구성할 때 만드는 자바 타입 
// 데이터의 특정한 상태를 나타낼 때 주로 사용 
@Getter
public enum AccountRole {
    // Security에서는 권한 정보를 조회할 때 "ROLE_" 접두어를 같이 조회 
    ROLE_ADMIN(1, "관리자"),
    ROLE_USER(2, "사용자");

    private final int id;
    private final String gradeName;

    private AccountRole(int id, String gradeName) {
        this.id = id;
        this.gradeName = gradeName;
    }

    // id에 맞는 AccountRolke 객체를 리턴 
    public static AccountRole fromId(int id) {
        for(AccountRole role : AccountRole.values()) {
            if(role.getId() == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 아이디" + id);
    }
}
