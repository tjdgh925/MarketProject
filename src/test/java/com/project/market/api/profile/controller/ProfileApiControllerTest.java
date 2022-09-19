package com.project.market.api.profile.controller;

import com.google.gson.Gson;
import com.project.market.api.profile.dto.ProfileResponseDto;
import com.project.market.api.profile.dto.ProfileUpdateDto;
import com.project.market.api.profile.service.ProfileApiService;
import com.project.market.domain.member.entity.Member;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProfileApiControllerTest {


    @InjectMocks
    private ProfileApiController target;

    @Mock
    private ProfileApiService profileApiService;

    @Mock
    private ProfileUpdateDto profileUpdateDto;

    private MockMvc mockMvc;

    private Gson gson;

    final String getEmailUrl = "/api/email";
    final String profileUrl = "/api/profile";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(target)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
        gson = new Gson();
    }


    @Test
    public void 회원이메일조회테스트_성공() throws Exception {
        //given
        doReturn("member").when(profileApiService).getEmailByToken("token");


        //when
        ResultActions resultActions = mockMvc.perform(get(getEmailUrl)
                .header("Authorization", "token")
        ).andDo(print());

        //then
        resultActions.andExpect(
                result -> assertThat(result.getResponse().getContentAsString()).isEqualTo("member")
        );
    }

    @Test
    public void 회원정보조회테스트_성공() throws Exception {
        //given
        ProfileResponseDto response = ProfileResponseDto.builder().email("email").memberName("name").address("address").build();
        doReturn(response).when(profileApiService).getMemberByToken("token");


        //when
        ResultActions resultActions = mockMvc.perform(get(profileUrl)
                .header("Authorization", "token")
        ).andDo(print());

        resultActions.andExpect(
                result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(gson.toJson(response))
        );
    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            "이름, ",
            ", 주소",
    })
    public void 회원수정테스트_실패(String memberName, String address) throws Exception {
        //given
        ProfileUpdateDto profileUpdateDto = new ProfileUpdateDto(memberName, address);
        String content = gson.toJson(profileUpdateDto);

        //when
        ResultActions resultActions = mockMvc.perform(patch(profileUrl)
                .header("Authorization", "token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andDo(print());

        //then
        resultActions
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result -> assertTrue(result.getResolvedException() instanceof InvalidParameterException)
                );
    }

    @ParameterizedTest
    @CsvSource({
            "이름, 주소",
    })
    public void 회원수정테스트_성공(String memberName, String address) throws Exception {
        //given
        ProfileUpdateDto profileUpdateDto = new ProfileUpdateDto(memberName, address);
        String content = gson.toJson(profileUpdateDto);

        //when
        ResultActions resultActions = mockMvc.perform(patch(profileUrl)
                .header("Authorization", "token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andDo(print());

        //then
        resultActions
                .andExpect(status().isOk());
    }
}