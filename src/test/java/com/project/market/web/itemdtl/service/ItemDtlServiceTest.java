package com.project.market.web.itemdtl.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ItemDtlServiceTest {

    @InjectMocks
    private ItemDtlService target;

    @Mock
    private ItemService itemService;

    @Test
    public void 상품상세내용조회테스트_실패() throws Exception {
        //given
        doThrow(new BusinessException(ErrorCode.NO_MATCHING_ITEM)).when(itemService).findItemById(any(Long.class));

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.getItemDtl(1L));

        //then
        assertThat(result).isInstanceOf(BusinessException.class);
    }

    @Test
    public void 상품상세내용조회테스트_성공() throws Exception {
        //given
        final Item item = Item.builder()
                .itemName("name")
                .itemDetail("detail")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(123)
                .stockNumber(123)
                .imageList(new ArrayList<>())
                .build();
        doReturn(item).when(itemService).findItemById(any(Long.class));

        //when
        ItemDtlDto result = target.getItemDtl(1L);

        //then
        assertThat(result.getItemName()).isEqualTo(item.getItemName());
    }

}