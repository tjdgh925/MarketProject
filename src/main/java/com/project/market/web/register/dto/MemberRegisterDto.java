package com.project.market.web.register.dto;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRegisterDto {

    private String name;

    private String email;

    private String address;
    private String password;

    private String password2;


    public Member toEntity() {
        return Member.builder()
                .memberName(name)
                .memberType(MemberType.EMAIL)
                .email(email)
                .password(password)
                .role(MemberRole.ADMIN)
                .build();
    }
}
