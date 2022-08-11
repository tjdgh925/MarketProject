package com.project.market.domain.cart.service;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.cart.repository.CartRepository;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.order.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    //장바구니 조회하기
    @Transactional
    public Cart getCartByMember(Member member) {
        Optional<Cart> cart = cartRepository.findFirstByMember(member);
        if(cart.isEmpty())
                return createNewCart(member);
        return cart.get();

    }

    //장바구니에 상품 추가
    @Transactional
    public void addOrderItem(OrderItem orderItem, Member member) {
        Cart cart = getCartByMember(member);
        cart.addOrderItem(orderItem);
    }

    private Cart createNewCart(Member member) {
        return cartRepository.save(
                Cart.builder()
                        .member(member)
                        .orderItemList(new ArrayList<>())
                        .build()
        );
    }
}
