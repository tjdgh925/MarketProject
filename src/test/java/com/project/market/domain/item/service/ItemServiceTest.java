package com.project.market.domain.item.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.adminItem.dto.AdminItemHistDto;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService target;

    @Mock
    private ItemRepository itemRepository;

    final Member member = Member.builder()
            .email("test@email.com")
            .address("서울특별시")
            .memberName("tester")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();

    @Test
    public void 상품등록테스트_실패() throws Exception {
        //given
        final Item item = Item.builder()
                .itemName("상품명")
                .itemDetail("상품설명")
                .stockNumber(2)
                .member(member)
                .build();

        //when
        Item result = target.saveItem(item);

        //then
        assertThat(result).isNull();
    }


    @Test
    public void 상픔등록테스트_성공() throws Exception {
        //given
        final Item item = Item.builder()
                .itemName("상품명")
                .itemDetail("상품설명")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(300)
                .stockNumber(2)
                .member(member)
                .build();
        doReturn(item).when(itemRepository).save(any(Item.class));

        //when
        Item result = target.saveItem(item);

        //then
        assertThat(result).isEqualTo(item);
    }

    @Test
    public void 상품조회테스트_실패() throws Exception {
        //given
        final long id = 1L;
        doReturn(Optional.empty()).when(itemRepository).findById(any(long.class));

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.findItemById(id));

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NO_MATCHING_ITEM.getMessage());
    }

    @Test
    public void 상품조회테스트_성공() throws Exception {
        //given
        final long id = 1L;
        final Item item = Item.builder()
                .itemName("상품명")
                .itemDetail("상품설명")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(300)
                .stockNumber(2)
                .member(member)
                .build();
        doReturn(Optional.of(item)).when(itemRepository).findById(id);

        //when
        Item result = target.findItemById(id);

        //then
        assertThat(item).isEqualTo(result);
    }

    @Test
    public void 상품수정기능테스트_실패() throws Exception {
        //given
        doReturn(Optional.empty()).when(itemRepository).findById(any(Long.class));
        final Item update = Item.builder()
                .itemName("상품명")
                .itemDetail("상품설명")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(300)
                .stockNumber(2)
                .member(member)
                .build();

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.updateItem(1L, update));

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NO_MATCHING_ITEM.getMessage());
    }

    @Test
    public void 상품수정기능테스트_성공() throws Exception {
        //given
        final Item saved = Item.builder()
                .itemName("before")
                .itemDetail("before")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(10)
                .stockNumber(1)
                .member(member)
                .build();

        final Item change = Item.builder()
                .itemName("after")
                .itemDetail("after")
                .itemSellStatus(ItemSellStatus.SELL)
                .price(20)
                .stockNumber(2)
                .build();

        doReturn(Optional.of(saved)).when(itemRepository).findById(any(Long.class));

        //when
        Item result = target.updateItem(1L, change);

        //then
        assertThat(result.getItemName()).isEqualTo("after");
    }

    @Test
    public void 상품정보페이지조회테스트_실패() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);
        doReturn(null).when(itemRepository).getItemHistPage(member, pageable);


        //when
        Page<AdminItemHistDto> result = target.getAdminItemHistory(member, pageable);

        //then
        assertThat(result).isNull();
    }
    
    @Test
    public void 상품정보페이지조회테스트_성공() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);
        List<AdminItemHistDto> hist = new ArrayList<>();
        hist.add(AdminItemHistDto.builder()
                .build());
        PageImpl<AdminItemHistDto> page = new PageImpl<>(hist);
        doReturn(page).when(itemRepository).getItemHistPage(member, pageable);
        
        //when
        Page<AdminItemHistDto> result = target.getAdminItemHistory(member, pageable);

        //then
        assertThat(result.getTotalPages()).isEqualTo(1);
    }
}