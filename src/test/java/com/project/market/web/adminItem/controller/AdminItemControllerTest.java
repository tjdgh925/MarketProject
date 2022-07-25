package com.project.market.web.adminItem.controller;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.web.adminItem.dto.AdminItemDto;
import com.project.market.web.adminItem.service.AdminItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.NestedServletException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class AdminItemControllerTest {

    @InjectMocks
    private AdminItemController target;

    @Mock
    private AdminItemService adminItemService;

    @Mock
    private Principal principal;

    private MockMvc mockMvc;

    final String itemName = "itemName";
    final Integer price = 3000;
    final String itemDetail = "details";
    final Integer stockNumber = 100;
    final ItemSellStatus itemSellStatus = ItemSellStatus.SOLD_OUT;
    final String fileName = "testImage";
    final String contentType = "png";
    final String filePath = "src/test/resources/image/testImage.png";


    AdminItemDto.Register adminItemDto = AdminItemDto.Register.builder()
            .itemName("itemName")
            .price(30)
            .itemDetail("details")
            .stockNumber(30)
            .itemSellStatus(ItemSellStatus.SOLD_OUT)
            .build();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .build();
    }

    @Test
    public void 상품등록뷰반환테스트() throws Exception {
        //given
        final String url = "/admin/items/new";
        final String view = "adminitem/registeritemform";

        //when
        ResultActions resultActions = mockMvc.perform(get(url))
                .andDo(print());

        //then
        resultActions
                .andExpect(model().attributeExists("adminItemDto"))
                .andExpect(view().name(view));
    }

    @Test
    public void 상품등록테스트_실패_대표이미지_없을경우() throws Exception {
        //given
        final String url = "/admin/items/new";

        List<MultipartFile> imageFiles = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            imageFiles.add(getMockMultiFile(fileName, contentType, filePath));
        adminItemDto.setItemImageFiles(imageFiles);

        //when
        NestedServletException result = assertThrows(NestedServletException.class, () -> mockMvc.perform(post(url)
                .param("itemName", itemName)
                .param("price", String.valueOf(price))
                .param("itemDetail", itemDetail)
                .param("stockNumber", String.valueOf(stockNumber))
                .param("itemSellStatus", itemSellStatus.name())
                .param("itemImageFiles", String.valueOf(new ArrayList<MultipartFile>()))
                .principal(principal)
        ).andDo(print()));


        //then
        assertThat(result.getClass()).isEqualTo(NestedServletException.class);
    }

    private MockMultipartFile getMockMultiFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}