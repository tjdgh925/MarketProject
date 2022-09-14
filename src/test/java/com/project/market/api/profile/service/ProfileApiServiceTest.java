package com.project.market.api.profile.service;

import com.project.market.global.config.security.jwt.JwtTokenProvider;
import com.project.market.global.error.exception.TokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ProfileApiServiceTest {

    @InjectMocks
    private ProfileApiService target;

    @Mock
    private JwtTokenProvider tokenProvider;

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

}