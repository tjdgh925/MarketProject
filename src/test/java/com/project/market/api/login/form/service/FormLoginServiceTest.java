package com.project.market.api.login.form.service;

import com.project.market.api.login.form.dto.FormRegisterDto;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private PasswordEncoder passwordEncoder;

    private final String email = "test@email.com";
    private final String memberName = "tester";
    private final String address = "서울특별시";
    private final String password = "password";

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
}