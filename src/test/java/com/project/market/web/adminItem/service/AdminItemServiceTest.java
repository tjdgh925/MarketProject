package com.project.market.web.adminItem.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.domain.itemImage.entity.ItemImage;
import com.project.market.domain.itemImage.service.ItemImageService;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.adminItem.dto.RegisterAdminItemDto;
import com.project.market.web.adminItem.dto.UpdateAdminItemDto;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        RegisterAdminItemDto adminItemDto = RegisterAdminItemDto.builder()
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

        RegisterAdminItemDto adminItemDto = RegisterAdminItemDto.builder()
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

    @Test
    public void 아이템조회테스트_실패() throws Exception {
        //given
        doReturn(Item.builder().build()).when(itemService).findItemById(any(long.class));
        doReturn(null).when(itemImageService).findImagesByItem(any(Item.class));

        //when
        NullPointerException result = assertThrows(NullPointerException.class, () -> target.getItemAndImages(1L));

        //then
        assertThat(result.getClass()).isEqualTo(NullPointerException.class);
    }

    @Test
    public void 아이템조회테스트_성공() throws Exception {
        //given
        List<MultipartFile> itemImageList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            itemImageList.add(getMockMultiFile(fileName, contentType, filePath));
        List<ItemImage> itemImages = toEntity(itemImageList);

        doReturn(item).when(itemService).findItemById(any(long.class));
        doReturn(itemImages).when(itemImageService).findImagesByItem(any(Item.class));

        //when
        UpdateAdminItemDto updateDto = target.getItemAndImages(1L);

        //then
        assertThat(updateDto.getItemName()).isEqualTo(item.getItemName());
        updateDto.getItemImageDtos().stream().forEach(
                itemImageDto ->
                        assertThat(itemImageDto.getOriginalImageName()).isEqualTo(itemImages.get(0).getOriginalImageName())
        );
    }

    @Test
    public void 상품정보수정테스트_성공() throws Exception {
        //given
        List<MultipartFile> itemImageList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            itemImageList.add(getMockMultiFile(fileName, contentType, filePath));
        List<ItemImage> itemImages = toEntity(itemImageList);

        UpdateAdminItemDto updateDto = UpdateAdminItemDto.builder()
                .itemId(1L)
                .itemName("ItemName")
                .price(12)
                .itemDetail("ItemDetails")
                .stockNumber(11)
                .itemSellStatus(ItemSellStatus.SOLD_OUT)
                .build();

        //when
        target.updateItem(updateDto);

        //then
        verify(itemService, times(1)).updateItem(any(Long.class), any(Item.class));

    }

    private MockMultipartFile getMockMultiFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    private List<ItemImage> toEntity(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(
                multipartFile -> ItemImage.builder()
                        .imageName(multipartFile.getName())
                        .imageUrl("url")
                        .isRepImage(false)
                        .item(item)
                        .originalImageName(multipartFile.getOriginalFilename())
                        .build()
        ).collect(Collectors.toList());
    }
}