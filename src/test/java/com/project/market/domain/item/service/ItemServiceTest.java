package com.project.market.domain.item.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

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
}