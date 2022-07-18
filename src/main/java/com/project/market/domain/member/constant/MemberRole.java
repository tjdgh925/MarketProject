package com.project.market.domain.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("ROLE_ADMIN", "판매자"),
    USER("ROLE_USER", "사용자");

    private final String key;
    private final String title;

}
