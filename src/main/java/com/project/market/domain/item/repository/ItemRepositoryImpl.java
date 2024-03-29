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

import java.util.ArrayList;
import java.util.List;

import static com.project.market.domain.item.entity.QItem.item;
import static com.project.market.domain.itemImage.entity.QItemImage.itemImage;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminItemHistDto> getItemHistPage(Member member, Pageable pageable) {
        Long size = queryFactory.select(item.count())
                .from(item)
                .join(item.imageList, itemImage)
                .on(eqRepImage())
                .where(eqMember(member))
                .fetchOne();

        List<AdminItemHistDto> result = new ArrayList<>();

        if (size > 0) {
            result = queryFactory.select(
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
                    .on(eqRepImage())
                    .where(eqMember(member))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
        
        return new PageImpl<AdminItemHistDto>(result, pageable, size);
    }

    @Override
    public Page<MainItemDto> getMainItemPage(String searchQuery, Pageable pageable) {
        Long size = queryFactory.select(item.count())
                .from(item)
                .join(item.imageList, itemImage)
                .on(eqRepImage())
                .where(eqItemSearch(searchQuery))
                .fetchOne();

        List<MainItemDto> result = new ArrayList<>();

        if (size > 0) {
            result = queryFactory.select(
                            Projections.constructor(
                                    MainItemDto.class,
                                    item.id,
                                    item.itemName,
                                    item.itemDetail,
                                    itemImage.imageUrl,
                                    item.price
                            )).from(item)
                    .join(item.imageList, itemImage)
                    .on(eqRepImage())
                    .where(eqItemSearch(searchQuery))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        return new PageImpl<MainItemDto>(result, pageable, size);
    }

    private BooleanExpression eqItemSearch(String searchQuery) {
        if (StringUtils.isEmpty(searchQuery)) {
            return eqStatus_SELL();
        }
       return (item.itemDetail.contains(searchQuery)
                .or(item.itemName.contains(searchQuery)))
               .and(eqStatus_SELL());
    }


    private BooleanExpression eqMember(Member member) {
        return item.member.eq(member);
    }

    private BooleanExpression eqRepImage() {
        return itemImage.isRepImage.eq(true);
    }

    private BooleanExpression eqStatus_SELL() {
        return item.itemSellStatus.eq(ItemSellStatus.SELL);
    }
}
