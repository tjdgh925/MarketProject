package com.project.market.api.profile.service;

import com.project.market.api.profile.dto.ProfileResponseDto;
import com.project.market.api.profile.dto.ProfileUpdateDto;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.error.exception.TokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileApiServiceTest {

    @InjectMocks
    private ProfileApiService target;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private MemberService memberService;
    @Test
    public void 이메일조회테스트_실패() throws Exception {
        //given
        String token = "token";

        //when
        TokenException result = assertThrows(TokenException.class, () -> target.getEmailByToken(token));

        //then
        assertThat(result).isInstanceOf(TokenException.class);
    }

    @Test
    public void 이메일조회테스트_성공() throws Exception {
        //given
        String token = "Bearer token";
        doReturn("email").when(tokenProvider).getEmail("token");

        //when
        String result = target.getEmailByToken(token);

        //then
        assertThat(result).isEqualTo("email");
    }

    @Test
    public void 회원정보조회테스트_실패() throws Exception {
        //given
        String token = "token";

        //when
        TokenException result = assertThrows(TokenException.class, () -> target.getMemberByToken(token));

        //then
        assertThat(result).isInstanceOf(TokenException.class);
    }

    @Test
    public void 회원정보조회테스트_성공() throws Exception {
        //given
        Member member = Member.builder().email("email").build();
        String token = "Bearer token";
        doReturn("email").when(tokenProvider).getEmail("token");
        doReturn(member).when(memberService).findMember("email");

        //when
        ProfileResponseDto result = target.getMemberByToken(token);

        //then
        assertThat(result.getEmail()).isEqualTo("email");
    }

    @Test
    public void 회원정보수정테스트_실패() throws Exception {
        //given
        String token = "token";
        ProfileUpdateDto profileUpdateDto = new ProfileUpdateDto();

        //when
        TokenException result = assertThrows(TokenException.class, () -> target.updateProfile(token, profileUpdateDto));

        //then
        assertThat(result).isInstanceOf(TokenException.class);
    }


    @Test
    public void 회원정보수정테스트_성공() throws Exception {
        //given
        String token = "Bearer token";
        ProfileUpdateDto profileUpdateDto = new ProfileUpdateDto("memberName", "address");
        doReturn("email").when(tokenProvider).getEmail("token");

        //when
        target.updateProfile(token, profileUpdateDto);

        //then
        verify(memberService, times(1)).update(anyString(), anyString(), anyString());
    }
}