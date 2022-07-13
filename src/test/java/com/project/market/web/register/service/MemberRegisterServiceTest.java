package com.project.market.web.register.service;

import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.register.dto.MemberRegisterDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberRegisterServiceTest {

    @InjectMocks
    private MemberRegisterService target;
    @Mock
    private MemberService memberService;

    private final String email = "test@email.com";
    private final String memberName = "tester";
    private final String password = "password";
    private final String address = "서울특별시";

    @Test
    public void 회원가입실패_다른비밀번호() throws Exception {
        //given
        MemberRegisterDto registerDto = MemberRegisterDto.builder()
                .email(email)
                .name(memberName)
                .address(address)
                .password(password)
                .password2("newPass")
                .build();

        //when
        final BusinessException result = assertThrows(BusinessException.class, () -> target.register(registerDto));

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.PASSWORD_NOT_SAME.getMessage());
    }

    @Test
    public void 회원가입성공테스트() throws Exception {
        //given
        MemberRegisterDto registerDto = MemberRegisterDto.builder()
                .email(email)
                .name(memberName)
                .address(address)
                .password(password)
                .password2(password)
                .build();


        //when
        target.register(registerDto);

        //then
        verify(memberService, times(1)).register(any(Member.class));
    }

}