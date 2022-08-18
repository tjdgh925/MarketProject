package com.project.market.web.orderhist.service;


import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.orderhist.dto.OrderHistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class OrderHistService {

    private final MemberService memberService;
    private final OrderService orderService;

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderHistory(Principal principal, Pageable pageable) {
        Member member = getMember(principal);
        return orderService.getOrderHistPage(member, pageable);
    }

    private Member getMember(Principal principal) {
        return memberService.findByEmail(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_MATCHING_MEMBER));
    }
}
