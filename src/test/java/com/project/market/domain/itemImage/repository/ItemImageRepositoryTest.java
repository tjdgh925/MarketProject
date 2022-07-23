package com.project.market.domain.itemImage.repository;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.itemImage.entity.ItemImage;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemImageRepositoryTest {

    @Autowired
    private ItemImageRepository itemImageRepository;

    @Autowired
    private ItemRepository itemRepository;
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
    final Item item = Item.builder()
            .itemName("상품명")
            .itemDetail("상품설명")
            .itemSellStatus(ItemSellStatus.SOLD_OUT)
            .price(300)
            .stockNumber(2)
            .member(member)
            .build();

    @BeforeEach
    public void init(){
        memberRepository.save(member);
        itemRepository.save(item);
    }

    @Test
    public void 아이템이미지등록테스트_실패() throws Exception {
        //given
        final ItemImage itemImage = ItemImage.builder()
                .imageName("randomName")
                .originalImageName("image")
                .imageUrl("/market/image")
                .isRepImage(true)
                .build();

        //when

        //then
        assertThrows(DataIntegrityViolationException.class, () -> itemImageRepository.save(itemImage));
    }

    @Test
    public void 아이템이미지등록테스트_성공() throws Exception {
        //given
        final ItemImage itemImage = ItemImage.builder()
                .imageName("randomName")
                .originalImageName("image")
                .imageUrl("/market/image")
                .isRepImage(true)
                .item(item)
                .build();

        //when
        ItemImage result = itemImageRepository.save(itemImage);

        //then
        assertThat(result).isEqualTo(result);
    }

}