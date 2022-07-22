package com.project.market.web.adminItem.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.item.service.ItemService;
import com.project.market.domain.itemImage.service.ItemImageService;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.infra.image.FileService;
import com.project.market.web.adminItem.dto.AdminItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AdminItemServiceTest {

    @InjectMocks
    private AdminItemService target;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemImageService itemImageService;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private FileService fileService;

    @Mock
    private Principal principal;

    final Member member = Member.builder()
            .email("test")
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

    final String fileName = "testImage";
    final String contentType = "png";
    final String filePath = "src/test/resources/image/testImage.png";

    @BeforeEach
    public void init() {
        memberRepository.save(member);
    }

    @Test
    public void 아이템등록테스트_실패_회원이_존재하지_않을경우() throws Exception {
        //given
        doReturn("test").when(principal).getName();
        List<MultipartFile> imageFiles = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            imageFiles.add(getMockMultiFile(fileName, contentType, filePath));

        AdminItemDto adminItemDto = AdminItemDto.builder()
                .itemName("ItemName")
                .price(12)
                .itemDetail("ItemDetails")
                .stockNumber(11)
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .itemImageFiles(imageFiles)
                .build();

        //when
        BusinessException result = assertThrows(BusinessException.class, () -> target.addNewAdminItem(adminItemDto, principal));

        //then
        assertThat(result.getMessage()).isEqualTo(ErrorCode.NO_MATCHING_MEMBER.getMessage());
    }

    @Test
    public void 아이템등록테스트_성공() throws Exception {
        //given
        doReturn("test").when(principal).getName();
        doReturn(Optional.of(member)).when(memberService).findByEmail("test");
        List<MultipartFile> imageFiles = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            imageFiles.add(getMockMultiFile(fileName, contentType, filePath));

        AdminItemDto adminItemDto = AdminItemDto.builder()
                .itemName("ItemName")
                .price(12)
                .itemDetail("ItemDetails")
                .stockNumber(11)
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .itemImageFiles(imageFiles)
                .build();

        //when
        Long result = target.addNewAdminItem(adminItemDto, principal);

        //then
        assertThat(result).isEqualTo(item.getId());
    }

    private MockMultipartFile getMockMultiFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

}