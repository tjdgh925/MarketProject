package com.project.market.api.login.token.service;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.config.security.UserDetailsServiceImpl;
import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.TokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService target;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private MemberService memberService;

    private String accessBearerToken = "Bearer validAccessToken";
    private String accessToken = "validAccessToken";
    private String refreshBearerToken = "Bearer refreshToken";
    private String refreshToken = "refreshToken";

    @Test
    public void 토큰재발급테스트_accessToken유효할경우() throws Exception {
        //given
        doReturn(true).when(tokenProvider).validateToken(accessToken);

        //when
        TokenDto tokenDto = target.refreshToken(accessBearerToken, refreshBearerToken);

        //then
        assertThat(tokenDto.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    public void 토큰재발급테스트_refreshToken유효하지않을경우() throws Exception {
        //given
        doReturn(false).when(tokenProvider).validateToken(accessToken);
        doReturn(false).when(tokenProvider).validateToken(refreshToken);

        //when
        TokenException result = assertThrows(TokenException.class, () -> target.refreshToken(accessBearerToken, refreshBearerToken));

        //then
        assertThat(result).isInstanceOf(TokenException.class);
    }

    @Test
    public void 토큰재발급테스트_저장된토큰값과다를경우() throws Exception {
        //given
        doReturn(false).when(tokenProvider).validateToken(accessToken);
        doReturn(true).when(tokenProvider).validateToken(refreshToken);
        doReturn("email").when(tokenProvider).getEmail(refreshToken);
        doReturn("different").when(memberService).getRefreshTokenByEmail(anyString());

        //when
        TokenException result = assertThrows(TokenException.class, () -> target.refreshToken(accessBearerToken, refreshBearerToken));

        //then
        assertThat(result).isInstanceOf(TokenException.class);
    }

    @Test
    public void 토큰재발급테스트_성공() throws Exception {
        //given
        doReturn(false).when(tokenProvider).validateToken(accessToken);
        doReturn(true).when(tokenProvider).validateToken(refreshToken);
        doReturn("email").when(tokenProvider).getEmail(refreshToken);
        doReturn(refreshToken).when(memberService).getRefreshTokenByEmail(anyString());
        doReturn(Member.builder().build()).when(memberService).findMember(anyString());
        doReturn(TokenDto.builder()
                .accessToken("newAccess")
                .refreshToken("newRefresh")
                .build())
        .when(tokenProvider).createTokenDto(any(Member.class));

        //when
        TokenDto tokenDto = target.refreshToken(accessBearerToken, refreshBearerToken);

        //then
        assertThat(tokenDto.getAccessToken()).isEqualTo("newAccess");
    }
}

