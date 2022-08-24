package com.project.market.web.cartList.controller;

import com.google.gson.Gson;
import com.project.market.global.error.exception.ControllerExceptionHandler;
import com.project.market.global.error.exception.DtoEmptyException;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.web.cartList.dto.CartOrderDto;
import com.project.market.web.cartList.service.CartListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartListRestControllerTest {

    @InjectMocks
    private CartListRestController target;

    @Mock
    private CartListService cartListService;

    @Mock
    private Principal principal;

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
    public void 상품주문테스트_실패() throws Exception {
        //given
        final String url = "/cart/orders";
        CartOrderDto cartOrderDto = new CartOrderDto();
//        cartOrderDto.setId(1L);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(cartOrderDto))
                        .principal(principal))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().is4xxClientError()
        );

    }
    @Test
    public void 상품주문테스트_성공() throws Exception {
        //given
        final String url = "/cart/orders";
        CartOrderDto cartOrderDto = getCartOrderDto();

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(cartOrderDto))
                        .principal(principal))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().isOk()
        );

    }

    @Test
    public void 상품수량수정테스트_실패() throws Exception {
        //given
        final String url = "/orderItem/1?count=3";
        doThrow(EntityNotFoundException.class).when(cartListService).changeItemCount(anyLong(),anyInt());

        //when
        ResultActions resultActions = mockMvc.perform(patch(url))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().is4xxClientError()
        );
    }

    @Test
    public void 상품수량수정테스트_성공() throws Exception {
        //given
        final String url = "/orderItem/1?count=3";

        //when
        ResultActions resultActions = mockMvc.perform(patch(url))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().isOk()
        );
        verify(cartListService, times(1)).changeItemCount(anyLong(), anyInt());
    }

    @Test
    public void 장바구니주문취소테스트_실패() throws Exception {
        //given
        final String url = "/orderItem/";

        //when
        ResultActions resultActions = mockMvc.perform(delete(url))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().is4xxClientError());
    }

    @Test
    public void 장바구니주문취소테스트_성공() throws Exception {
        //given
        final String url = "/orderItem/1";

        //when
        ResultActions resultActions = mockMvc.perform(delete(url))
                .andDo(print());

        //then
        resultActions.andExpect(
                status().isOk());
        verify(cartListService, times(1)).cancelOrderItem(anyLong());
    }

    private CartOrderDto getCartOrderDto() {
        CartOrderDto cartOrderDto = new CartOrderDto();
        List<CartOrderDto> cartOrderDtoList = new ArrayList<>();
        CartOrderDto cartOrderDto1 = new CartOrderDto();
//        cartOrderDto1.setId(1L);
        CartOrderDto cartOrderDto2 = new CartOrderDto();
//        cartOrderDto2.setId(2L);
        cartOrderDtoList.add(cartOrderDto1);
        cartOrderDtoList.add(cartOrderDto2);
//        cartOrderDto.setCartOrderDtoList(cartOrderDtoList);
        return cartOrderDto;
    }

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }
}