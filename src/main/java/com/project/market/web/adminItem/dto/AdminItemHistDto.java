package com.project.market.web.adminItem.dto;

import com.project.market.domain.item.constant.ItemSellStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class AdminItemHistDto {

    private Long itemId;

    private String itemName;

    private String itemDetail;

    private LocalDateTime registerTime;
    private int stockNumber;

    private int price;

    private ItemSellStatus itemSellStatus;

    private String imageUrl;

    public AdminItemHistDto(Long itemId, String itemName, String itemDetail, LocalDateTime registerTime, int stockNumber, int price, ItemSellStatus itemSellStatus, String imageUrl) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.registerTime = registerTime;
        this.stockNumber = stockNumber;
        this.price = price;
        this.itemSellStatus = itemSellStatus;
        this.imageUrl = imageUrl;
    }
}
