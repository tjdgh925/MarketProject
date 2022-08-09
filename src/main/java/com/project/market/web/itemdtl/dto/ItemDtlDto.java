package com.project.market.web.itemdtl.dto;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.item.entity.Item;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class ItemDtlDto {

    private Long itemId;

    private String itemName;

    private Integer price;

    private String itemDetail;

    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImageDto> itemImageDtos;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class ItemImageDto {
        private String imageUrl;
    }

    public static ItemDtlDto of(Item item) {
        item.getImageList().stream().map(itemImage -> itemImage.getImageUrl()).collect(Collectors.toList());

        return ItemDtlDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .itemDetail(item.getItemDetail())
                .stockNumber(item.getStockNumber())
                .itemSellStatus(item.getItemSellStatus())
                .itemImageDtos(
                        item.getImageList().stream().map(itemImage ->
                            ItemImageDto.builder()
                                .imageUrl(itemImage.getImageUrl())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}
