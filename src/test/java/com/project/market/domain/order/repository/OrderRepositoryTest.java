package com.project.market.domain.order.repository;

import com.project.market.TestConfig;
import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.order.constant.OrderStatus;
import com.project.market.domain.order.entity.Order;
import com.project.market.global.config.jpa.AuditConfig;
import com.project.market.web.main.dto.MainItemDto;
import com.project.market.web.orderhist.dto.OrderHistDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AuditConfig.class, TestConfig.class})
class OrderRepositoryTest {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;


    final Member member = Member.builder()
            .email("test@email.com")
            .address("서울특별시")
            .memberName("tester")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();

    @BeforeEach
    public void init() {
        memberRepository.save(member);
    }

    @Test
    public void 주문등록테스트_실패() throws Exception {
        //given
        LocalDateTime time = LocalDateTime.now();
        Order order = Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .orderTime(time)
                .build();

        //when
        DataIntegrityViolationException result = assertThrows(DataIntegrityViolationException.class, () -> orderRepository.save(order));

        //then
        assertThat(result).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void 주문등록테스트_성공() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        Order order = Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .orderTime(time)
                .build();

        //when
        Order result = orderRepository.save(order);

        //then
        assertThat(result).isEqualTo(order);
    }

    @Test
    public void 주문이력조회테스트_실패() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);

        //when
        InvalidDataAccessApiUsageException result = assertThrows(InvalidDataAccessApiUsageException.class, () -> orderRepository.getOrderHistByMember(null, pageable));

        //then
        assertThat(result).isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    public void 메인화면상품조회테스트_성공() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);

        //when
        Page<OrderHistDto> result = orderRepository.getOrderHistByMember(member, pageable);

        //then
        assertThat(result).isNotNull();
    }
}