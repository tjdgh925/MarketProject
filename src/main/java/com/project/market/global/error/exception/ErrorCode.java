package com.project.market.global.error.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorCode {

    // 인증
    ALREADY_REGISTERED_MEMBER(400, "이미 가입된 회원 입니다."),
    PASSWORD_NOT_SAME(400, "비밀번호가 다릅니다."),
    NO_MATCHING_MEMBER(400, "해당 회원이 존재하지 않습니다."),
    LOGIN_ERROR(400, "로그인 오류 발생."),
    NOT_VALID_MEMBER_TYPE(400, "유효한 로그인 타입이 아닙니다."),
    WRONG_PASSWORD(400, "비밀번호를 확인해주세요."),
    NOT_VALID_TOKEN(403, "Token이 유효하지 않습니다."),
    TOKEN_EXPIRED(403, "Token이 만료되었습니다."),
    TOKNE_EMPTY(403, "Token이 존재하지 않습니다."),

    // 상품(Item)
    NO_REP_IMAGE(400, "대표 이미지를 등록해주세요."),
    ADD_ITEM_ERROR(400, "상품 등록중에 오류가 발생했습니다."),
    NO_MATCHING_ITEM(400, "해당 상품이 존재하지 않습니다."),
    UPDATE_ITEM_ERROR(400, "상품 수정중에 오류가 발생했습니다."),
    NOT_ENOUGH_STOCK(409, "상품의 재고가 부족합니다."),

    // 주문 (Order)
    NO_MATCHING_ORDER_ITEM(400, "주문 정보가 존재하지 않습니다."),
    CART_ITEM_NOT_SELECTED(400, "주문할 상품을 선택해주세요."),
    ;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;

    public static ErrorCode getErrorCodeByErrorMessage(String message) {
        return Arrays.stream(ErrorCode.values()).filter(e -> e.message.equals(message)).findFirst().orElse(null);
    }

}
