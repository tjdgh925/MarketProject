package com.project.market.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 인증
    ALREADY_REGISTERED_MEMBER(400, "이미 가입된 회원 입니다."),
    PASSWORD_NOT_SAME(400, "비밀번호가 다릅니다."),
    NO_MATCHING_MEMBER(400, "해당 회원이 존재하지 않습니다."),
    LOGIN_ERROR(400, "로그인 오류 발생."),

    NOT_VALID_MEMBER_TYPE(400, "유효한 로그인 타입이 아닙니다."),

    NO_REP_IMAGE(400, "대표 이미지를 등록해주세요."),

    ADD_ITEM_ERROR(400, "상품 등록중에 오류가 발생했습니다."),

    NO_MATCHING_ITEM(400, "해당 상품이 존재하지 않습니다."),
    UPDATE_ITEM_ERROR(400, "상품 수정중에 오류가 발생했습니다."),

    NOT_ENOUGH_STOCK(400, "상품의 재고가 부족합니다."),

    NO_MATCHING_ORDER_ITEM(400, "주문 정보가 존재하지 않습니다."),

    ;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;

}
