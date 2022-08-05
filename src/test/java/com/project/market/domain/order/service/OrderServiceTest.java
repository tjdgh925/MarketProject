package com.project.market.domain.order.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.order.entity.Order;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.repository.OrderItemRepository;
import com.project.market.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService target;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MemberRepository memberRepository;
    
    final Member member = Member.builder()
            .email("test@email.com")
            .address("서울특별시")
            .memberName("tester")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();

    Item item = Item.builder()
            .itemName("상품명")
            .itemDetail("상품설명")
            .itemSellStatus(ItemSellStatus.SELL)
            .price(300)
            .stockNumber(2)
            .member(member)
            .build();

    @BeforeEach
    public void init() {
        memberRepository.save(member);
    }


    @Test
    public void 상품주문테스트_실패() throws Exception {
        //given
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = null;
        orderItemList.add(orderItem);

        //when
        NullPointerException result = assertThrows(NullPointerException.class, () -> target.registerOrder(member, orderItemList));

        //then
        assertThat(result).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void 상품주문테스트_성공() throws Exception {
        //given
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem =OrderItem.builder()
                        .item(item)
                        .orderPrice(300)
                        .count(1)
                        .build();
        orderItemList.add(orderItem);

        //when
        target.registerOrder(member, orderItemList);

        //then
        verify(orderRepository, times(1)).save(any(Order.class));
    }


}