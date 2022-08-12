package com.project.market.web.itemdtl.controller;

import com.google.gson.Gson;
import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.member.entity.Member;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import com.project.market.web.itemdtl.dto.RegisterOrderDto;
import com.project.market.web.itemdtl.service.ItemDtlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemDtlControllerTest {


    @InjectMocks
    private ItemDtlController target;

    @Mock
    private ItemDtlService itemDtlService;

    @Mock
    private Principal principal;

    private Gson gson;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
        gson = new Gson();
    }

    @Test
    public void 상품상세내용조회테스트() throws Exception {
        //given
        final String url = "/itemdtl/1";
        final String view = "itemdtl/itemdtl";

        final ItemDtlDto item = ItemDtlDto.builder()
                .itemName("name")
                .itemDetail("detail")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(123)
                .stockNumber(123)
                .itemImageDtos(new ArrayList<>())
                .build();

        doReturn(item).when(itemDtlService).getItemDtl(any(Long.class));

        //when
        ResultActions resultActions = mockMvc.perform(get(url)).andDo(print());

        //then
        resultActions
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name(view));
    }

    @Test
    public void 주문등록테스트() throws Exception {
        //given
        final String url = "/itemdtl/order";
        RegisterOrderDto registerOrderDto = RegisterOrderDto.builder().itemId(1L).count(3).build();
        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .content(gson.toJson(registerOrderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .principal(principal)
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk());
    }

}