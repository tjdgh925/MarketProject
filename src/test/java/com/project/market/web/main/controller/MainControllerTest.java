package com.project.market.web.main.controller;

import com.project.market.web.adminItem.dto.AdminItemHistDto;
import com.project.market.web.adminItem.service.AdminItemService;
import com.project.market.web.main.dto.ItemSearchDto;
import com.project.market.web.main.dto.MainItemDto;
import com.project.market.web.main.service.MainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @InjectMocks
    private MainController target;

    @Mock
    private MainService mainService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void 메인화면뷰반환테스트() throws Exception {
        final String url = "/";
        String searchQuery = "";
        Pageable pageable = PageRequest.of(0, 6);
        ItemSearchDto itemSearchDto = ItemSearchDto.builder()
                .searchQuery(searchQuery)
                .build();

        List<MainItemDto> hist = new ArrayList<>();
        hist.add(MainItemDto.builder()
                .build());
        PageImpl<MainItemDto> page = new PageImpl<>(hist);

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("itemSearchDto", String.valueOf(itemSearchDto))
                .param("page", String.valueOf(1))
                .param("model", String.valueOf(page))
        ).andDo(print());

        //then
        resultActions
                .andExpect(view().name("main/mainpage")
                );

    }

}