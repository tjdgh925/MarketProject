package com.project.market.web.orderhist.controller;

import com.project.market.web.orderhist.service.OrderHistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class OrderHistControllerTest {

    @InjectMocks
    private OrderHistController target;

    @Mock
    private OrderHistService orderHistService;

    @Mock
    private Principal principal;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void 주문이력조회테스트() throws Exception {
        //given
        final String url = "/orderhist";

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("page", String.valueOf(1))
                .principal(principal)
        ).andDo(print());

        //then
        resultActions
                .andExpect(view().name("orderhist/orderhist"))
        ;
    }
}