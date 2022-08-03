package com.project.market.web.itemdtl.controller;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import com.project.market.web.itemdtl.service.ItemDtlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class ItemDtlControllerTest {


    @InjectMocks
    private ItemDtlController target;

    @Mock
    private ItemDtlService itemDtlService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
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
}