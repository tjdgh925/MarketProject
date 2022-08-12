package com.project.market.web.itemdtl.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.member.service.MemberService;
import com.project.market.domain.order.entity.Order;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.service.OrderService;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.itemdtl.dto.ItemDtlDto;
import com.project.market.web.itemdtl.dto.RegisterOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemDtlService {

    private final ItemService itemService;

    private final OrderService orderService;

    private final MemberService memberService;

    public ItemDtlDto getItemDtl(Long itemId) {

        Item item = itemService.findItemById(itemId);

        return ItemDtlDto.of(item);
    }

    @Transactional
    public void registerOrderItem(RegisterOrderDto registerOrderDto, Principal principal) {
        Member member = memberService.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_MATCHING_MEMBER));
        Item item = itemService.findItemById(registerOrderDto.getItemId());

        List<OrderItem> orderItemList = new ArrayList<>();
        addOrderItem(orderItemList, item, registerOrderDto.getCount());

        orderService.registerOrder(member, orderItemList);
    }

    private void addOrderItem(List<OrderItem> orderItemList, Item item, int count){
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice() * count)
                .build();
        itemService.reduceStock(item, count);
        orderItemList.add(orderItem);
    }
}
