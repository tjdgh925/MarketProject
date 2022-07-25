package com.project.market.domain.itemImage.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.itemImage.entity.ItemImage;
import com.project.market.domain.itemImage.repository.ItemImageRepository;
import com.project.market.infra.image.FileService;
import com.project.market.infra.image.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemImageService {

    private final ItemImageRepository itemImageRepository;

    private final FileService fileService;
    private final String IMAGE_URL_PREFIX = "/images/";
    private final int SIZE = 5;

    @Transactional
    public void saveItemImages(List<MultipartFile> itemImageFiles, Item item) throws IOException {
        for (int i = 0; i < SIZE; i++) {
            Boolean isRepImage = i == 0;
            saveItemImage(item, itemImageFiles.get(i), isRepImage);
        }
    }

    @Transactional
    public void saveItemImage(Item item, MultipartFile itemImageFile, Boolean isRepImage) throws IOException {

        UploadFile uploadFile = fileService.storeFile(itemImageFile);
        String storeFileName = uploadFile != null ? uploadFile.getStoreFileName() : "";
        String originalFilename = uploadFile != null ? uploadFile.getOriginalFileName() : "";
        String imageUrl = uploadFile != null ? IMAGE_URL_PREFIX + storeFileName : "";

        ItemImage itemImage = ItemImage.builder()
                .imageName(storeFileName)
                .imageUrl(imageUrl)
                .originalImageName(originalFilename)
                .isRepImage(isRepImage)
                .build();

        itemImage.setItem(item);
        itemImageRepository.save(itemImage);
    }

    public List<ItemImage> findImagesByItem(Item item) {
        return itemImageRepository.findByItemOrderById(item);
    }

    @Transactional
    public void updateItemImage(ItemImage itemImage, MultipartFile updateImage) throws IOException {

        if (!itemImage.getImageName().isEmpty()) {
            fileService.deleteFile(itemImage.getImageUrl());
        }

        UploadFile uploadFile = fileService.storeFile(updateImage);
        String originalFileName = uploadFile.getOriginalFileName();
        String storeFileName = uploadFile.getStoreFileName();
        String imageUrl = IMAGE_URL_PREFIX + storeFileName;

        itemImage.updateImage(originalFileName, storeFileName, imageUrl);

    }

    @Transactional
    public void deleteItemImage(ItemImage itemImage) {
        String uploadPath = fileService.getFullFileUploadPath(itemImage.getImageName());
        fileService.deleteFile(uploadPath);

        itemImage.refresh();
    }
}
