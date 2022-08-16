package com.project.market.web.cartList.controller;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.cart.repository.CartRepository;
import com.project.market.domain.cart.service.CartService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.service.OrderService;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class CartListController {
    private final CartListService cartListService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {

        List<CartListItemDto> cartItems = cartListService.getCartList(principal);

        model.addAttribute("cartItems", cartItems);

        return "cart/cartlist";
    }
}