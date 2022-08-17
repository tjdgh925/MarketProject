package com.project.market.domain.order.repository;

import com.project.market.domain.member.entity.Member;
import com.project.market.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom{

    List<Order> findByMember(Member member);

}
