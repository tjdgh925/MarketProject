package com.project.market.web.itemdtl.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDtlService {

    private final ItemService itemService;

    public ItemDtlDto getItemDtl(Long itemId) {

        Item item = itemService.findItemById(itemId);

        return ItemDtlDto.of(item);
    }
}
