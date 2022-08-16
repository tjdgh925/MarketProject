package com.project.market.web.cartList.service;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.cart.service.CartService;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.web.cartList.dto.CartListItemDto;
import com.project.market.web.cartList.dto.CartOrderDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartListServiceTest {

    @InjectMocks
    private CartListService target;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @Mock
    private MemberService memberService;

    @Mock
    private Principal principal;

    @Mock
    private Item item = Item.builder().stockNumber(10).build();

    List<OrderItem> orderItemList = new ArrayList<>();

    final Cart cart = Cart.builder()
            .orderItemList(orderItemList)
            .build();

    @Test
    public void 장바구니목록조회테스트_실패() throws Exception {
        //given
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(Member.builder().build())).when(memberService).findByEmail(anyString());
        doReturn(Cart.builder().build()).when(cartService).getCartByMember(any(Member.class));

        //when
        NullPointerException result = assertThrows(NullPointerException.class, () -> target.getCartList(principal));

        //then
        assertThat(result).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void 장바구니목록조회테스트_성공() throws Exception {
        //given
        doReturn(Optional.of(Member.builder().build())).when(memberService).findByEmail(anyString());
        doReturn("test").when(principal).getName();
        doReturn(cart).when(cartService).getCartByMember(any(Member.class));

        //when
        List<CartListItemDto> result = target.getCartList(principal);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    public void 장바구니주문테스트_실패() throws Exception {
        //given
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(Member.builder().build())).when(memberService).findByEmail(anyString());
        doThrow(BusinessException.class).when(orderService).findOrderItemById(anyLong());

        List<CartOrderDto> cartOrderDtoList = new ArrayList<>();
        CartOrderDto cartOrderDto = new CartOrderDto();
        cartOrderDto.setId(1L);
        cartOrderDtoList.add(cartOrderDto);

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.orderCartItems(cartOrderDtoList, principal));

        //then
        assertThat(result).isInstanceOf(BusinessException.class);
    }

    @Test
    public void 장바구니주문테스트_성공() throws Exception {
        //given
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(Member.builder().build())).when(memberService).findByEmail(anyString());
        doReturn(OrderItem.builder().build()).when(orderService).findOrderItemById(anyLong());

        List<CartOrderDto> cartOrderDtoList = new ArrayList<>();
        CartOrderDto cartOrderDto = new CartOrderDto();
        cartOrderDto.setId(1L);
        cartOrderDtoList.add(cartOrderDto);

        //when
        target.orderCartItems(cartOrderDtoList, principal);

        //then
        verify(orderService, times(1)).registerOrder(any(Member.class), anyList());
    }

    @Test
    public void 주문상품수량변경테스트_실패() throws Exception {
        //given
        doThrow(BusinessException.class).when(orderService).findOrderItemById(anyLong());

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.changeItemCount(1L, 1));

        //then
        assertThat(result).isInstanceOf(BusinessException.class);
    }

    @Test
    public void 주문상품수량변경테스트_성공() throws Exception {
        //given
        OrderItem orderItem = OrderItem.builder().item(item).count(15).build();
        doReturn(orderItem).when(orderService).findOrderItemById(anyLong());

        //when
        target.changeItemCount(1L, 11);

        //then
        verify(item, times(1)).increaseStock(anyInt());
    }

    @Test
    public void 주문상품취소테스트_성공() throws Exception {
        //given

        //when
        target.cancelOrderItem(1L);

        //then
        verify(orderService, times(1)).deleteOrderItemById(1L);

    }
}