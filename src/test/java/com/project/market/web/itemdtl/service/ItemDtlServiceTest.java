package com.project.market.web.itemdtl.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import com.project.market.web.itemdtl.dto.RegisterOrderDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemDtlServiceTest {

    @InjectMocks
    private ItemDtlService target;

    @Mock
    private ItemService itemService;

    @Mock
    private Principal principal;

    @Mock
    private OrderService orderService;

    @Mock
    private MemberService memberService;

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

    @Test
    public void 상품등록테스트_실패() throws Exception {
        //given
        doReturn("test").when(principal).getName();
        doReturn(Optional.empty()).when(memberService).findByEmail(anyString());
        RegisterOrderDto registerOrderDto = RegisterOrderDto.builder().itemId(1L).count(3).build();

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.registerOrderItem(registerOrderDto, principal));

        //then
        assertThat(result).isInstanceOf(BusinessException.class);
    }

    @Test
    public void 상품등록테스트_성공() throws Exception {
        //given
        Member member = Member.builder().build();
        Item item = Item.builder().price(100).build();
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(member)).when(memberService).findByEmail(anyString());
        doReturn(item).when(itemService).findItemById(anyLong());
        RegisterOrderDto registerOrderDto = RegisterOrderDto.builder().itemId(1L).count(3).build();

        //when
        target.registerOrderItem(registerOrderDto, principal);

        //then
        verify(orderService, times(1)).registerOrder(any(Member.class), anyList());
    }

}