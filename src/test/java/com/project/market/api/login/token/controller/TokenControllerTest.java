package com.project.market.api.login.token.controller;

import com.google.gson.Gson;
import com.project.market.api.login.token.service.TokenService;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.ControllerExceptionHandler;
import com.project.market.global.error.exception.TokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @InjectMocks
    private TokenController target;

    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;

    private Gson gson;

    final String url = "/api/refreshtoken";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(target)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
        gson = new Gson();
    }

    @Test
    public void 토큰재발급테스트_실패() throws Exception {
        //given
        doThrow(TokenException.class).when(tokenService).refreshToken(anyString(), anyString());

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header("Authorization", "accessToken")
                .header("refreshToken", "refreshToken")
        ).andDo(print());

        //then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result ->{
                            assertThat(result.getResolvedException() instanceof TokenException);
                      }
        );
    }

    @Test
    public void 토큰재발급테스트_성공() throws Exception {
        //given
        doReturn(TokenDto.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build()).when(tokenService).refreshToken(anyString(), anyString());

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header("Authorization", "accessToken")
                .header("refreshToken", "refreshToken")
        ).andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
    }
}