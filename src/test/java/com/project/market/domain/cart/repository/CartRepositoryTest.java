package com.project.market.domain.cart.repository;

import com.project.market.TestConfig;
import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.global.config.jpa.AuditConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AuditConfig.class, TestConfig.class})
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

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

    Cart insert = Cart.builder()
            .member(member)
            .orderItemList(null)
            .build();

    @BeforeEach
    public void init() {
        memberRepository.save(member);
        cartRepository.save(insert);
    }
    @Test
    public void CartRespository가_NULL이_아님() throws Exception {
        //given


        //when

        //then
        assertThat(cartRepository).isNotNull();
    }

    @Test
    public void 장바구니등록테스트_실패() throws Exception {
        //given
        Cart cart = Cart.builder()
                .orderItemList(null)
                .build();

        //when
        DataIntegrityViolationException result = assertThrows(DataIntegrityViolationException.class, () -> cartRepository.save(cart));


        //then
        assertThat(result).isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void 장바구니등록테스트_성공() throws Exception {
        //given
        Cart cart = Cart.builder()
                .member(member)
                .orderItemList(null)
                .build();

        //when
        Cart result = cartRepository.save(cart);


        //then
        assertThat(result).isEqualTo(cart);
    }

    @Test
    public void 장바구니조회테스트_실패() throws Exception {
        //given

        //when
        Optional<Cart> result = cartRepository.findFirstByMember(null);

        //then
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void 장바구니조회테스트_성공() throws Exception {
        //given

        //when
        Optional<Cart> result = cartRepository.findFirstByMember(member);

        //then
        assertThat(result).isEqualTo(Optional.of(insert));
    }

    @Test
    public void 장바구니삭제테스트_실패() throws Exception {
        //given


        //when
        cartRepository.deleteAllByMember(null);
        Optional<Cart> result = cartRepository.findFirstByMember(member);

        //then
        assertThat(result).isEqualTo(Optional.of(insert));

    }

    @Test
    public void 장바구니삭제테스트_성공() throws Exception {
        //given


        //when
        cartRepository.deleteAllByMember(member);
        Optional<Cart> result = cartRepository.findFirstByMember(member);

        //then
        assertThat(result).isEqualTo(Optional.empty());

    }


}