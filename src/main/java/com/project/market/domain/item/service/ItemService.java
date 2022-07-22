package com.project.market.domain.item.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }
}
