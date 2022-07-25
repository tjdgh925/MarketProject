package com.project.market.web.adminItem.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.domain.itemImage.entity.ItemImage;
import com.project.market.domain.itemImage.service.ItemImageService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.adminItem.dto.RegisterAdminItemDto;
import com.project.market.web.adminItem.dto.UpdateAdminItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminItemService {

    private final ItemService itemService;
    private final ItemImageService itemImageService;
    private final MemberService memberService;

    @Transactional
    public Long addNewAdminItem(RegisterAdminItemDto adminItemDto, Principal principal) throws IOException {
        Member member = findMemberByPrincipal(principal);
        Item item = adminItemDto.toItemEntity(member);
        itemService.saveItem(item);

        itemImageService.saveItemImages(adminItemDto.getItemImageFiles(), item);
        return item.getId();
    }

    private Member findMemberByPrincipal(Principal principal) {
        String email = principal.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_MATCHING_MEMBER));
        return member;
    }

    public UpdateAdminItemDto getItemAndImages(long itemId) {
        Item item = itemService.findItemById(itemId);
        List<UpdateAdminItemDto.ItemImageDto> itemImageDtos = getItemImageDtos(item);

        return UpdateAdminItemDto.of(item, itemImageDtos);
    }

    private List<UpdateAdminItemDto.ItemImageDto> getItemImageDtos(Item item) {
        List<ItemImage> imagesList = itemImageService.findImagesByItem(item);
        return imagesList.stream().map(
                image ->
                        UpdateAdminItemDto.ItemImageDto.builder()
                                .itemImageId(image.getId())
                                .originalImageName(image.getOriginalImageName())
                                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void updateItem(UpdateAdminItemDto updateItemDto) throws IOException {
        Item updateItem = itemService.updateItem(updateItemDto.getItemId(), updateItemDto.toItemEntity());
        updateItemImages(updateItem, updateItemDto);
    }

    private void updateItemImages(Item item, UpdateAdminItemDto updateItemDto) throws IOException {
        List<ItemImage> itemImages = itemImageService.findImagesByItem(item);
        List<String> originalImageNames = updateItemDto.getOriginalImageNames();
        List<MultipartFile> itemImageFiles = updateItemDto.getItemImageFiles();

        for (int i = 0; i < itemImages.size(); i++) {
            ItemImage itemImage = itemImages.get(i);
            String imageName = originalImageNames.get(i);
            MultipartFile imageFile = itemImageFiles.get(i);

            if (!imageFile.isEmpty()) {
                itemImageService.updateItemImage(itemImage, imageFile);
            } else if(imageName.isEmpty() && !itemImage.getOriginalImageName().isEmpty()){
                itemImageService.deleteItemImage(itemImage);
            }

        }

    }
}
