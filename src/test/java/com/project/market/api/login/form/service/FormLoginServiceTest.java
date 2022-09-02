package com.project.market.api.login.form.service;

import com.project.market.api.login.form.dto.FormLoginRequestDto;
import com.project.market.api.login.form.dto.FormRegisterDto;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.global.error.exception.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormLoginServiceTest {

    @InjectMocks
    private FormLoginService target;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final String email = "test@email.com";
    private final String memberName = "tester";
    private final String address = "서울특별시";
    private final String password = "password";

//
//    @BeforeEach
//    public void init() {
//        memberRepository.save(sample);
//    }

    @Test
    public void 회원가입실패_이미존재하는회원() throws Exception {
        //given
        FormRegisterDto sample = new FormRegisterDto(email, memberName, address, password, password);
        doThrow(EntityNotFoundException.class).when(memberService).register(any(Member.class));

        //when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> target.registerMember(sample));

        //then
        assertThat(result).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void 회원가입테스트_성공() throws Exception {
        //given
        FormRegisterDto sample = new FormRegisterDto(email, memberName, address, password, password);

        //when
        target.registerMember(sample);

        //then
        verify(memberService, times(1)).register(any(Member.class));
    }

    @Test
    public void 로그인테스트_실패() throws Exception {
        //given
        Member sample = Member.builder().email(email)
                .password(passwordEncoder.encode(password))
                .build();
        FormLoginRequestDto formRequestDto = new FormLoginRequestDto(email, "wrong");
        doReturn(sample).when(memberService).findMember(email);

        //when
        InvalidParameterException result = assertThrows(InvalidParameterException.class, () -> target.formLogin(formRequestDto));

        //then
        assertThat(result).isInstanceOf(InvalidParameterException.class);
    }
}