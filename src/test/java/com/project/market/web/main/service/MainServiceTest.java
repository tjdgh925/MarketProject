package com.project.market.web.main.service;

import com.project.market.domain.item.service.ItemService;
import com.project.market.web.main.dto.ItemSearchDto;
import com.project.market.web.main.dto.MainItemDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @InjectMocks
    private MainService target;

    @Mock
    private ItemService itemService;

    @Test
    public void 메인화면상품조회테스트_실패() throws Exception {
        //given
        String searchQuery = "";
        Pageable pageable = PageRequest.of(0, 6);
        ItemSearchDto itemSearchDto = ItemSearchDto.builder()
                .searchQuery(searchQuery)
                .build();
        doReturn(null).when(itemService).getSearchMainItem(searchQuery, pageable);


        //when
        Page<MainItemDto> result = target.getMainItem(itemSearchDto, pageable);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void 메인화면상품조회테스트_성공() throws Exception {
        //given
        String searchQuery = "";
        Pageable pageable = PageRequest.of(0, 6);
        ItemSearchDto itemSearchDto = ItemSearchDto.builder()
                .searchQuery(searchQuery)
                .build();
        List<MainItemDto> hist = new ArrayList<>();
        hist.add(MainItemDto.builder()
                .build());
        PageImpl<MainItemDto> page = new PageImpl<>(hist);
        doReturn(page).when(itemService).getSearchMainItem(searchQuery, pageable);

        //when
        Page<MainItemDto> result = target.getMainItem(itemSearchDto, pageable);

        //then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }
}