package com.project.market.domain.order.repository;

import com.project.market.TestConfig;
import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.order.entity.Order;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.global.config.jpa.AuditConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AuditConfig.class, TestConfig.class})
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    final Member member = Member.builder()
            .email("test@email.com")
            .address("서울특별시")
            .memberName("tester")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();

    Item item = Item.builder()
            .itemName("상품명")
            .itemDetail("상품설명")
            .itemSellStatus(ItemSellStatus.SELL)
            .price(300)
            .stockNumber(2)
            .member(member)
            .build();

    @BeforeEach
    public void init() {
        memberRepository.save(member);
        itemRepository.save(item);
    }

    @Test
    public void 주문등록테스트_실패() throws Exception {
        //given
        final OrderItem orderItem = OrderItem.builder()
                .orderPrice(300)
                .count(3)
                .build();

        //when
        DataIntegrityViolationException result = assertThrows(DataIntegrityViolationException.class, () -> orderItemRepository.save(orderItem));

        //then
        assertThat(result).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void 주문등록테스트_성공() throws Exception {
        //given
        final OrderItem orderItem = OrderItem.builder()
                .orderPrice(300)
                .count(3)
                .item(item)
                .build();

        //when
        OrderItem result = orderItemRepository.save(orderItem);

        //then
        assertThat(result).isEqualTo(orderItem);
    }

}