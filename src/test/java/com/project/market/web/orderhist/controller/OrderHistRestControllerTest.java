package com.project.market.web.orderhist.controller;

import com.google.gson.Gson;
import com.project.market.global.error.exception.ControllerExceptionHandler;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.web.orderhist.service.OrderHistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderHistRestControllerTest {

    @InjectMocks
    private OrderHistRestController target;

    @Mock
    private OrderHistService orderHistService;

    private Gson gson;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(target)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
        gson = new Gson();
    }

    @Test
    public void 주문취소테스트_실패() throws Exception {
        //given
        final String url = "/orderhist/1/cancel";
        doThrow(EntityNotFoundException.class).when(orderHistService).cancelOrder(anyLong());

        //when
        ResultActions resultActions = mockMvc.perform(patch(url))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    public void 주문취소테스트_성공() throws Exception {
        //given
        final String url = "/orderhist/1/cancel";

        //when
        ResultActions resultActions = mockMvc.perform(patch(url))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().isOk()
        );
        verify(orderHistService, times(1)).cancelOrder(anyLong());
    }
}