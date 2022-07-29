package com.project.market.domain.item.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.repository.ItemRepository;
import com.project.market.domain.member.entity.Member;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.adminItem.dto.AdminItemHistDto;
import com.project.market.web.main.dto.MainItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Item findItemById(Long itemId) {

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_MATCHING_ITEM));
    }

    @Transactional
    public Item updateItem(Long itemId, Item updateItem) {
        Item item = findItemById(itemId);
        item.update(updateItem);
        return item;
    }

    public Page<AdminItemHistDto> getAdminItemHistory(Member member, Pageable pageable) {
        return itemRepository.getItemHistPage(member, pageable);
    }

    public Page<MainItemDto> getSearchMainItem(String searchQuery, Pageable pageable) {
        return itemRepository.getMainItemPage(searchQuery, pageable);
    }
}
