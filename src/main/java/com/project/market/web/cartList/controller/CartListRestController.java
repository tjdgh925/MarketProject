package com.project.market.web.cartList.controller;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.cart.repository.CartRepository;
import com.project.market.domain.cart.service.CartService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.DtoEmptyException;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.cartList.dto.CartListItemDto;
import com.project.market.web.cartList.dto.CartOrderDto;
import com.project.market.web.cartList.service.CartListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CartListRestController {
    private final CartListService cartListService;

    @PatchMapping("/orderItem/{id}")
    public ResponseEntity change(@PathVariable Long id,
                                 @RequestParam int count) {

        cartListService.changeItemCount(id, count);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/cart/orders")
    public ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            throw new DtoEmptyException(ErrorCode.CART_ITEM_NOT_SELECTED);
        }

        cartListService.orderCartItems(cartOrderDtoList, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/orderItem/{id}")
    public ResponseEntity cancelCartItem(@PathVariable Long id) {
        cartListService.cancelOrderItem(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
