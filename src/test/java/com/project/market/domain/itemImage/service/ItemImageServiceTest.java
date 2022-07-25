package com.project.market.domain.itemImage.service;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.itemImage.entity.ItemImage;
import com.project.market.domain.itemImage.repository.ItemImageRepository;
import com.project.market.domain.member.constant.MemberRole;
import com.project.market.domain.member.constant.MemberType;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.repository.MemberRepository;
import com.project.market.infra.image.FileService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemImageServiceTest {

    @InjectMocks
    private ItemImageService target;

    @Mock
    private ItemImageRepository itemImageRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FileService fileService;

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

    final String fileName = "testImage";
    final String contentType = "png";
    final String filePath = "src/test/resources/image/testImage.png";

    @BeforeEach
    public void init(){
        memberRepository.save(member);
        itemRepository.save(item);
    }

    @Test
    public void MockMultiFile테스트() throws IOException {
        //given

        //when
        MockMultipartFile mockMultipartFile = getMockMultiFile(fileName, contentType, filePath);

        //then
        assertThat(mockMultipartFile.getOriginalFilename()).isEqualTo(fileName + "." + contentType);
        assertThat(mockMultipartFile.getContentType()).isEqualTo(contentType);
    }

    @Test
    public void 아이템이미지등록테스트_실패() throws Exception {
        //given
        List<MultipartFile> itemImageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            itemImageList.add(null);
        }

        //when
        IndexOutOfBoundsException result = assertThrows(IndexOutOfBoundsException.class, () -> target.saveItemImages(itemImageList, item));

        //then
        assertThat(result.getClass()).isEqualTo(IndexOutOfBoundsException.class);
    }

    @Test
    public void 아이템이미지등록테스트_성공() throws Exception {
        //given
        List<MultipartFile> itemImageList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            itemImageList.add(getMockMultiFile(fileName, contentType, filePath));

        //when
        target.saveItemImages(itemImageList, item);

        //then
        verify(itemImageRepository, times(5)).save(any(ItemImage.class));

    }

    @Test
    public void 아이템이미지조회테스트_실패() throws Exception {
        //given
        doReturn(new ArrayList<ItemImage>()).when(itemImageRepository).findByItemOrderById(item);

        //when
        List<ItemImage> result = target.findImagesByItem(item);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void 아이템이미지조회테스트_성공() throws Exception {
        //given
        List<MultipartFile> itemImageList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            itemImageList.add(getMockMultiFile(fileName, contentType, filePath));
        List<ItemImage> itemImages = toEntity(itemImageList);
        doReturn(itemImages).when(itemImageRepository).findByItemOrderById(item);

        //when
        List<ItemImage> result = target.findImagesByItem(item);

        //then
        assertThat(result).isEqualTo(itemImages);
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