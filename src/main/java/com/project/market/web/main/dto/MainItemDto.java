package com.project.market.web.main.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MainItemDto {

    private Long itemId;

    private String itemName;

    private String itemDetail;

    private String imageUrl;

    private Integer price;

    public MainItemDto(Long itemId, String itemName, String itemDetail, String imageUrl, Integer price) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemDetail = itemDetail;
        this.imageUrl = imageUrl;
        this.price = price;
    }

}
