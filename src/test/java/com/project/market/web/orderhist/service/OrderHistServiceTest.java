package com.project.market.web.orderhist.service;

import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.constant.OrderStatus;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.web.orderhist.dto.OrderHistDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHistServiceTest {

    @InjectMocks
    private OrderHistService target;

    @Mock
    private OrderService orderService;

    @Mock
    private MemberService memberService;

    @Mock
    private Principal principal;

    final Member member = Member.builder()
            .email("test@email.com")
            .address("서울특별시")
            .memberName("tester")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();

    @Test
    public void 상품이력조회_실패() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(Member.builder().build())).when(memberService).findByEmail("test");

        //when
        Page<OrderHistDto> result = target.getOrderHistory(principal, pageable);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void 상품이력조회_성공() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(member)).when(memberService).findByEmail("test");

        OrderHistDto orderHistDto = new OrderHistDto(1L, LocalDateTime.now(), OrderStatus.ORDER);
        List<OrderHistDto> list = new ArrayList<>();
        list.add(orderHistDto);
        PageImpl<OrderHistDto> page = new PageImpl<>(list);
        doReturn(page).when(orderService).getOrderHistPage(member, pageable);

        //when
        Page<OrderHistDto> result = target.getOrderHistory(principal, pageable);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    public void 주문취소테스트_실패() throws Exception {
        //given
        doThrow(EntityNotFoundException.class).when(orderService).cancelOrder(anyLong());

        //when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> target.cancelOrder(1L));

        //then
        assertThat(result).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void 주문취소테스트_성공() throws Exception {
        //given

        //when
        target.cancelOrder(1L);

        //then
        verify(orderService, times(1)).cancelOrder(anyLong());
    }
}