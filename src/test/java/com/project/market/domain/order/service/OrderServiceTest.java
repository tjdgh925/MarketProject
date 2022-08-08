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
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService target;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

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

    OrderItem orderItem =  OrderItem.builder()
            .orderPrice(300)
            .count(3)
            .item(item)
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

    @Test
    public void 주문조회테스트_싫패() throws Exception {
        //given
        doReturn(Optional.empty()).when(orderItemRepository).findById(any(Long.class));

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.findOrderItemById(1L));

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NO_MATCHING_ORDER_ITEM.getMessage());
    }

    @Test
    public void 주문조회테스트_성공() throws Exception {
        //given
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(any(Long.class));

        //when
        OrderItem result = target.findOrderItemById(1L);

        //then
        assertThat(result).isEqualTo(orderItem);
    }

    @Test
    public void 주문삭제테스트_실패() throws Exception {
        //given
        doThrow(EmptyResultDataAccessException.class).when(orderItemRepository).deleteById(any(Long.class));

        //when
        EmptyResultDataAccessException result = assertThrows(EmptyResultDataAccessException.class, () -> target.deleteOrderItemById(2L));

        //then
        assertThat(result).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void 주문삭제테스트_성공() throws Exception {
        //given
        doReturn(Optional.empty()).when(orderItemRepository).findById(any(Long.class));

        //when
        target.deleteOrderItemById(orderItem.getId());
        BusinessException result = assertThrows(BusinessException.class, () -> target.findOrderItemById(1L));

        //then
        assertThat(result).isInstanceOf(BusinessException.class);
    }

    @Test
    public void 주문개수변경_성공() throws Exception {
        //given
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(orderItem.getId());

        //when
        OrderItem result = target.findOrderItemById(orderItem.getId());
        target.changeOrderItemCount(result, 15);

        //then
        assertThat(result.getCount()).isEqualTo(15);
    }

}