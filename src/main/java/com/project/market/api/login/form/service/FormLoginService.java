package com.project.market.api.login.form.service;

import com.project.market.api.login.form.dto.FormLoginRequestDto;
import com.project.market.api.login.form.dto.FormRegisterDto;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.global.error.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormLoginService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public Member registerMember(FormRegisterDto formRegisterDto) {
        Member newMember = formRegisterDto.toEntity();
        memberService.register(newMember);
        return newMember;
    }

    @Transactional
    public TokenDto formLogin(FormLoginRequestDto formLoginDto){
        Member member = memberService.findMember(formLoginDto.getEmail());
        verifyPassword(member, formLoginDto);

        TokenDto tokenDto = tokenProvider.createTokenDto(member);
        String refreshToken = tokenDto.getRefreshToken();
        memberService.saveRefreshToken(member, refreshToken);

        return tokenDto;
    }

    private void verifyPassword(Member member, FormLoginRequestDto formLoginDto) {
        if(!passwordEncoder.matches(formLoginDto.getPassword(), member.getPassword())){
            throw new InvalidParameterException(ErrorCode.WRONG_PASSWORD.getMessage());
        }
    }

}
