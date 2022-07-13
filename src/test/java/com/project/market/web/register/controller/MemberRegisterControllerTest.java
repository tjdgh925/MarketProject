package com.project.market.web.register.controller;

import com.project.market.web.register.dto.MemberRegisterDto;
import com.project.market.web.register.service.MemberRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberRegisterControllerTest {

    @InjectMocks
    private MemberRegisterController target;

    @Mock
    private MemberRegisterService memberRegisterService;


    private MockMvc mockMvc;
    private final String email = "test@email.com";
    private final String memberName = "tester";
    private final String password = "12341234";
    private final String address = "서울특별시";



    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void 회원가입뷰반환테스트() throws Exception {
        //given
        final String url = "/register";
        final String view = "register/registerform";

        //when
        ResultActions resultActions = this.mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .accept(MediaType.parseMediaType("application/html;charset=UTF-8")));

        //then
        resultActions
                .andExpect(model().attributeExists("memberRegisterDto"))
                .andExpect(view().name(view));
    }

    @Test
    public void 회원가입성공테스트() throws Exception {
        //given
        final String url = "/register";
        final String view = "redirect:/login";

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .param("name", memberName)
                .param("address", address)
                .param("email", email)
                .param("password", password)
                .param("password2", password)
        ).andDo(print());

        //then
        resultActions
                .andExpect(model().attributeExists("memberRegisterDto"))
                .andExpect(view().name(view));
    }

    @Test
    public void 회원가입실패_유효성검증() throws Exception {
        //given
        final String url = "/register";
        final String view = "register/registerform";


        //비밀번호 길이 짧음
        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .param("name", memberName)
                .param("address", address)
                .param("email", email)
                .param("password", "1234")
                .param("password2", "1234")
        ).andDo(print());

        //then
        resultActions
                .andExpect(model().errorCount(1))
                .andExpect(view().name(view));

    }

}