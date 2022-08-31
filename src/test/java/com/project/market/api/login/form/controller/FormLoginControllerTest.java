package com.project.market.api.login.form.controller;

import com.google.gson.Gson;
import com.project.market.api.login.form.dto.FormLoginRequestDto;
import com.project.market.api.login.form.dto.FormRegisterDto;
import com.project.market.api.login.form.service.FormLoginService;
import com.project.market.api.login.form.validator.FormRegisterValidator;
import com.project.market.domain.member.entity.Member;
import com.project.market.global.config.security.jwt.TokenDto;
import com.project.market.global.error.exception.ControllerExceptionHandler;
import com.project.market.global.error.exception.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FormLoginControllerTest {

    @InjectMocks
    private FormLoginController target;

    @Mock
    private FormLoginService formLoginService;

    @Mock
    private FormRegisterValidator formRegisterValidator;

    @Mock
    private Member member;

    private MockMvc mockMvc;

    private Gson gson;

    final String registerUrl = "/api/register";
    final String loginUrl = "/api/login";

    private final String email = "test@email.com";
    private final String memberName = "tester";
    private final String address = "서울특별시";
    private final String password = "password";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(target)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
        gson = new Gson();
    }

    @ParameterizedTest
    @CsvSource({
            "testEmail",
            "testEmail@test.", 
            "이메일@email.com",
            "test@AA.com"
    })
    public void 회원가입테스트_실패_유효성검증(String email) throws Exception {
        //given
        FormRegisterDto sample = new FormRegisterDto(memberName, email, address, password, password);
        String input = gson.toJson(sample);

        //when
        ResultActions resultActions = mockMvc.perform(post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)).andDo(print());

        //then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(
                    result -> assertTrue(result.getResolvedException() instanceof InvalidParameterException)
        );
    }

    @Test
    public void 회원가입테스트_성공() throws Exception {
        //given
        doReturn(Member.builder().memberName(memberName).build()).when(formLoginService).registerMember(any(FormRegisterDto.class));
        FormRegisterDto sample = new FormRegisterDto(memberName, email, address, password, password);
        String input = gson.toJson(sample);

        //when
        ResultActions resultActions = mockMvc.perform(post(registerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)).andDo(print());

        //then
        resultActions.andExpect(
                status().isOk()
        );
    }

    @ParameterizedTest
    @CsvSource({
            "testEmail, password",
            "testEmail@test, password",
            "이메일@email.com, password",
            "test@AA.com,  "
    })
    public void 로그인테스트_실패_유효성검증(String email, String password) throws Exception {
        //given
        FormLoginRequestDto sample = new FormLoginRequestDto(email, password);
        String input = gson.toJson(sample);

        //when
        ResultActions resultActions = mockMvc.perform(post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)).andDo(print());

        //then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result -> assertTrue(result.getResolvedException() instanceof InvalidParameterException)
                );
    }

    @Test
    public void 로그인테스트_성공() throws Exception {
        //given
        doReturn(TokenDto.builder().build()).when(formLoginService).formLogin(any(FormLoginRequestDto.class));
        FormLoginRequestDto sample = new FormLoginRequestDto(email, password);
        String input = gson.toJson(sample);

        //when
        ResultActions resultActions = mockMvc.perform(post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(input)).andDo(print());

        //then
        resultActions.andExpect(
                status().isOk()
        );
    }

}