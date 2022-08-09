package com.project.market.web.main.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ItemSearchDto {

    private String searchQuery = "";
}
