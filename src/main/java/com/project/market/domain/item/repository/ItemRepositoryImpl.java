package com.project.market.domain.item.repository;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.member.entity.Member;
import com.project.market.web.adminItem.dto.AdminItemHistDto;
import com.project.market.web.main.dto.MainItemDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.project.market.domain.item.entity.QItem.item;
import static com.project.market.domain.itemImage.entity.QItemImage.itemImage;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminItemHistDto> getItemHistPage(Member member, Pageable pageable) {
        List<AdminItemHistDto> result = queryFactory.select(
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
                .fetch();

        Long size = queryFactory.select(item.count())
                .from(item)
                .join(item.imageList, itemImage)
                .on(itemImage.isRepImage.eq(true))
                .where(item.member.eq(member))
                .fetchOne();

        return new PageImpl<AdminItemHistDto>(result, pageable, size);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(String searchQuery, Pageable pageable) {
        List<MainItemDto> result = queryFactory.select(
                        Projections.constructor(
                                MainItemDto.class,
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImage.imageUrl,
                                item.price
                        )).from(item)
                .join(item.imageList, itemImage)
                .on(itemImage.isRepImage.eq(true))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .and(eqItemSearch(searchQuery)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long size = queryFactory.select(item.count())
                .from(item)
                .join(item.imageList, itemImage)
                .on(itemImage.isRepImage.eq(true))
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)
                        .and(eqItemSearch(searchQuery)))
                .fetchOne();

        return new PageImpl<MainItemDto>(result, pageable, size);

    }

    private BooleanExpression eqItemSearch(String searchQuery) {
        if (StringUtils.isEmpty(searchQuery)) {
            return null;
        }
        return item.itemDetail.contains(searchQuery)
                .or(item.itemName.contains(searchQuery));
    }
}
