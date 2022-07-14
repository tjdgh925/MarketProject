package com.project.market.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 인증
    ALREADY_REGISTERED_MEMBER(400, "이미 가입된 회원 입니다."),
    PASSWORD_NOT_SAME(400, "비밀번호가 다릅니다."),
    NO_MATCHING_MEMBER(400, "해당 회원이 존재하지 않습니다."),
    LOGIN_ERROR(400, "로그인 오류 발생."),
    ;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;

}