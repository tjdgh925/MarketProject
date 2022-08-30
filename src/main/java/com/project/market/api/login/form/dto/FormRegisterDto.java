package com.project.market.api.login.form.dto;


import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FormRegisterDto {

    private String memberName;

    private String email;

    private String address;

    private String password;

    private String passwordConfirm;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .memberName(memberName)
                .address(address)
                .memberType(MemberType.EMAIL)
                .role(MemberRole.USER)
                .address(address)
                .password(password)
                .build();
    }

}
