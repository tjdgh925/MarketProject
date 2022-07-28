package com.project.market.domain.item.repository;

import com.project.market.domain.member.entity.Member;
import com.project.market.web.adminItem.dto.AdminItemHistDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.project.market.domain.item.entity.QItem.item;
import static com.project.market.domain.itemImage.entity.QItemImage.itemImage;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminItemHistDto> getItemHistPage(Member member, Pageable pageable) {
        QueryResults<AdminItemHistDto> queryResults = queryFactory.select(
                Projections.constructor(
                        AdminItemHistDto.class,
                        item.id,
                        item.itemName,
                        item.itemDetail,
                        item.createTime,
                        item.stockNumber,
                        item.price,
                        item.itemSellStatus,
                        itemImage.imageUrl
                )).from(item)
                .join(item.imageList, itemImage)
                .on(itemImage.isRepImage.eq(true))
                .where(item.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}
