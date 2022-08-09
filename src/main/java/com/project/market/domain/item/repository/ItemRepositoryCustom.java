package com.project.market.domain.item.repository;

import com.project.market.domain.member.entity.Member;
import com.project.market.web.adminItem.dto.AdminItemHistDto;
import com.project.market.web.main.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<AdminItemHistDto> getItemHistPage(Member member, Pageable pageable);
    Page<MainItemDto> getMainItemPage(String searchQuery, Pageable pageable);
}
