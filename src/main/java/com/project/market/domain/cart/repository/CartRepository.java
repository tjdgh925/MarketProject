package com.project.market.domain.cart.repository;

import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

     Optional<Cart> findFirstByMember(Member member);

     void deleteAllByMember(Member member);
}
