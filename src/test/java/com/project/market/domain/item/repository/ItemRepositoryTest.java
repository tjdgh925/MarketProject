package com.project.market.domain.item.repository;

import com.project.market.TestConfig;
import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.global.config.jpa.AuditConfig;
import com.project.market.web.adminItem.dto.AdminItemHistDto;
import com.project.market.web.main.dto.MainItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@DataJpaTest
@Import({AuditConfig.class, TestConfig.class})
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepositoryImpl itemRepositoryImpl;


    final Member member = Member.builder()
            .email("test@email.com")
            .address("서울특별시")
            .memberName("tester")
            .password("password")
            .memberType(MemberType.EMAIL)
            .role(MemberRole.USER)
            .build();

    Item insert = Item.builder()
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
        itemRepository.save(insert);
    }


    @Test
    public void ItemRepository가_NULL이_아님() throws Exception {
        //given


        //when

        //then
        assertThat(itemRepository).isNotNull();
    }

    @Test
    public void 상품등록테스트_실패() throws Exception {
        //given
        final Item item = Item.builder()
                .itemName("상품명")
                .itemDetail("상품설명")
                .stockNumber(2)
                .member(member)
                .build();
        //when

        //then
        assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));
    }

    @Test
    public void 상품등록테스트_성공() throws Exception {
        //given
        final Item item = Item.builder()
                .itemName("상품명")
                .itemDetail("상품설명")
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .price(300)
                .stockNumber(2)
                .member(member)
                .build();

        //when
        Item result = itemRepository.save(item);

        //then
        assertThat(result.getItemName()).isEqualTo(item.getItemName());
    }

    @Test
    public void 상품조회테스트_실패() throws Exception {
        //given
        final Long id = 3L;

        //when
        Item result = itemRepository.findById(id)
                .orElse(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void 상품조회테스트_성공() throws Exception {
        //given
        final Long id = 1L;

        //when
        Item result = itemRepository.findById(id)
                .orElse(null);

        //then
        assertThat(result).isEqualTo(insert);
    }

    @Test
    public void 상품과이미지조회테스트_실패() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);

        //when
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> itemRepositoryImpl.getItemHistPage(null, pageable));

        //then
        assertThat(result).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 상품과이미지조회테스트_성공() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);

        //when
        Page<AdminItemHistDto> result = itemRepositoryImpl.getItemHistPage(member, pageable);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    public void 메인화면상품조회테스트_성공() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 6);

        //when
        Page<MainItemDto> result = itemRepositoryImpl.getMainItemPage("", pageable);

        //then
        assertThat(result).isNotNull();
    }
}