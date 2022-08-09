package com.project.market.web.main.service;


import com.project.market.domain.item.service.ItemService;
import com.project.market.web.main.dto.ItemSearchDto;
import com.project.market.web.main.dto.MainItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MainService {

    private final ItemService itemService;

    public Page<MainItemDto> getMainItem(ItemSearchDto itemSearchDto, Pageable pageable) {
        String searchQuery = itemSearchDto.getSearchQuery();
        return itemService.getSearchMainItem(searchQuery, pageable);
    }
}
