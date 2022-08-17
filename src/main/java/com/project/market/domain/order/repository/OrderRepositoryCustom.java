package com.project.market.domain.order.repository;

import com.project.market.domain.member.entity.Member;
import com.project.market.web.orderhist.dto.OrderHistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface OrderRepositoryCustom {

    Page<OrderHistDto> getOrderHistByMember(Member member, Pageable pageable);
}
