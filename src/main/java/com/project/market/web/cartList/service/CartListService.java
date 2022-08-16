package com.project.market.web.cartList.service;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.cart.service.CartService;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.itemImage.service.ItemImageService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.global.error.exception.StockException;
import com.project.market.web.cartList.dto.CartListItemDto;
import com.project.market.web.cartList.dto.CartOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartListService {

    private final CartService cartService;
    private final OrderService orderService;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<CartListItemDto> getCartList(Principal principal) {
        Member member = getMember(principal);
        List<OrderItem> orderItemList = getOrderItemList(member);
        List<CartListItemDto> cartItemList = getCartItemList(orderItemList);

        return cartItemList;
    }

    @Transactional
    public void orderCartItems(List<CartOrderDto> cartOrderDtoList, Principal principal) {
        Member member = getMember(principal);
        List<OrderItem> orderItemList = getOrderItemList(cartOrderDtoList);

        orderService.registerOrder(member, orderItemList);
    }

    @Transactional
    public void changeItemCount(Long itemId, int count) {
        OrderItem orderItem = orderService.findOrderItemById(itemId);
        changeItemStock(count, orderItem);

        orderService.changeOrderItemCount(orderItem, count);
    }

    @Transactional
    public void cancelOrderItem(Long itemId) {
        orderService.deleteOrderItemById(itemId);
    }

    private Member getMember(Principal principal) {
        return memberService.findByEmail(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_MATCHING_MEMBER));
    }

    private List<OrderItem> getOrderItemList(Member member) {
        Cart cart = cartService.getCartByMember(member);
        List<OrderItem> orderItemList = cart.getOrderItemList();
        return orderItemList;
    }

    private List<CartListItemDto> getCartItemList(List<OrderItem> orderItemList) {
        List<CartListItemDto> cartListItemDtoList =
                orderItemList.stream().map(
                        orderItem -> CartListItemDto.of(orderItem)
                ).collect(Collectors.toList());
        return cartListItemDtoList;
    }
    private List<OrderItem> getOrderItemList(List<CartOrderDto> cartOrderDtoList) {
        List<Long> orderItemIdList = cartOrderDtoList.stream().map(
                orderItem -> orderItem.getId()
        ).collect(Collectors.toList());

        List<OrderItem> orderItemList = orderItemIdList.stream().map(
                orderItemId -> orderService.findOrderItemById(orderItemId)
        ).collect(Collectors.toList());

        return orderItemList;
    }

    private void changeItemStock(int count, OrderItem orderItem) {
        Item item = orderItem.getItem();
        Integer before = orderItem.getCount();
        int change;
        if (before > count) {
            change = before - count;
            item.increaseStock(change);
        } else if (before < count) {
            change = count - before;
            if (item.getStockNumber() - change <= 0){
                throw new StockException(item.getStockNumber());
            }
            item.reduceStock(change);
        }
    }
}
