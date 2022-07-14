package com.project.market.web.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @InjectMocks
    private LoginController target;

    private MockMvc mockMvc;

    private final String email = "test@email.com";
    private final String password = "password";


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void 로그인뷰봔환_테스트() throws Exception {
        //given
        final String url = "/login";
        final String view = "login/loginform";

        //when
        ResultActions resultActions = mockMvc.perform(get(url)).andDo(print());

        //then
        resultActions
                .andExpect(view().name(view));
    }

    @Test
    public void 로그인테스트_실패() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(formLogin().user(email).password(password));

        //then
        resultActions.andExpect(unauthenticated());
    }
}