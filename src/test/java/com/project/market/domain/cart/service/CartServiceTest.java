package com.project.market.domain.cart.service;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.cart.repository.CartRepository;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.order.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService target;

    @Mock
    private CartRepository cartRepository;
    List<OrderItem> itemList = new ArrayList<>();

    @Mock
    private Cart cart = Cart.builder()
            .orderItemList(itemList).build();;

    @Test
    public void 장바구니조회테스트_실패() throws Exception {
        //given

        //when
        Cart result = target.getCartByMember(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void 장바구니조회테스트_성공() throws Exception {
        //given
        Member member = Member.builder().build();
        doReturn(Optional.of(cart)).when(cartRepository).findFirstByMember(member);

        //when
        Cart result = target.getCartByMember(member);

        //then
        assertThat(result).isEqualTo(cart);
    }

    @Test
    public void 장바구니에상품추가테스트_실패() throws Exception {
        //given
        Member member = Member.builder().build();
        doReturn(Optional.empty()).when(cartRepository).findFirstByMember(member);

        //when
        NullPointerException result = assertThrows(NullPointerException.class, () -> target.addOrderItem(OrderItem.builder().build(), member));

        //then
        assertThat(result).isInstanceOf(NullPointerException.class);

    }

    @Test
    public void 장바구니에상품추가테스트_성공() throws Exception {
        //given
        Member member = Member.builder().build();
        doReturn(Optional.of(cart)).when(cartRepository).findFirstByMember(member);

        //when
        target.addOrderItem(OrderItem.builder().build(), member);

        //then
        verify(cart, times(1)).addOrderItem(any(OrderItem.class));
    }

}