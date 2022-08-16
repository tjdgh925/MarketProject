package com.project.market.web.cartList.dto;


import com.project.market.domain.item.entity.Item;
import com.project.market.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class CartListItemDto {

    private Long id; //장바구니 상품 아이디

    private String itemName; //상품명

    private String itemDetail;

    private int price; //상품 금액

    private int count; //수량

    private String imgUrl; //상품 이미지 경로

    public static CartListItemDto of(OrderItem orderItem) {
        Item item = orderItem.getItem();
        return CartListItemDto.builder()
                .id(orderItem.getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .price(item.getPrice())
                .count(orderItem.getCount())
                .imgUrl(item.getImageList().get(0).getImageUrl())
                .build();
    }
}
