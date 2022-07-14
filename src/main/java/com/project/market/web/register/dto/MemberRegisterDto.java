package com.project.market.web.register.dto;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    private String password2;


    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .memberName(name)
                .memberType(MemberType.EMAIL)
                .address(address)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(MemberRole.ADMIN)
                .build();
    }

    @Builder
    public MemberRegisterDto(String name, String address, String email, String password, String password2) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }
}
