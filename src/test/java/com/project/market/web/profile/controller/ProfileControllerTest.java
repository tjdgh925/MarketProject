package com.project.market.web.profile.controller;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.web.profile.dto.ProfileUpdateDto;
import com.project.market.web.profile.service.ProfileService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.xml.validation.Validator;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @InjectMocks
    private ProfileController target;

    @Mock
    private Principal principal;

    @Mock
    ProfileService profileService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void 프로필뷰반환테스트() throws Exception {
        //given
        when(principal.getName()).thenReturn("test");
        when(profileService.getMemberInfo("test")).thenReturn(Member.builder().build());
        final String url = "/profile";
        final String view = "/profile/profileform";

        //when
        ResultActions resultActions = mockMvc.perform(get(url).principal(principal)).andDo(print());

        //then
        resultActions
                .andExpect(model().attributeExists("profileUpdateDto"))
                .andExpect(view().name(view));
    }

    @Test
    public void 회원정보수정테스트_실패() throws Exception {
        //given
        final String url = "/profile";
        final String view = "profile/profileform";
        String name = null;
        String address = "address";

        //when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .param("name", name)
                        .param("address", address)
        ).andDo(print());

        //then
        resultActions.andExpect(view().name(view));
    }

    @Test
    public void 회원정보수정테스트_성공() throws Exception {
        //given
        when(principal.getName()).thenReturn("test");
        final String url = "/profile";
        final String view = "redirect:/";
        String name = "name";
        String address = "address";

        //when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .principal(principal)
                        .param("name", name)
                        .param("address", address)
        ).andDo(print());

        //then
        resultActions.andExpect(view().name(view));
    }
}