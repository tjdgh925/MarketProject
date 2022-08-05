package com.project.market.web.itemdtl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RegisterOrderDto {

    private Long itemId;

    private int count;

    @Builder
    public RegisterOrderDto(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

}
