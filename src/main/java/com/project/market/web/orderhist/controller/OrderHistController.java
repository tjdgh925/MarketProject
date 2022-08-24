package com.project.market.web.orderhist.controller;

import com.project.market.web.orderhist.dto.OrderHistDto;
import com.project.market.web.orderhist.service.OrderHistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orderhist")
public class OrderHistController {

    private final OrderHistService orderHistService;

    private final int MIN_CONTENT = 0;
    private final int MAX_CONTENT = 6;
    private final int MAX_PAGE = 5;

    @GetMapping
    public String getOrderHist(Optional<Integer> page, Model model, Principal principal) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : MIN_CONTENT, MAX_CONTENT);

        Page<OrderHistDto> orderHistDtos = orderHistService.getOrderHistory(principal, pageable);

        model.addAttribute("orders", orderHistDtos);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", MAX_PAGE);
        return "orderhist/orderhist";
    }


}
