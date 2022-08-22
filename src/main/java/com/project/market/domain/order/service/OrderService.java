package com.project.market.domain.order.service;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.item.service.ItemService;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.order.entity.Order;
import com.project.market.domain.order.entity.OrderItem;
import com.project.market.domain.order.repository.OrderItemRepository;
import com.project.market.domain.order.repository.OrderRepository;
import com.project.market.global.error.exception.BusinessException;
import com.project.market.global.error.exception.EntityNotFoundException;
import com.project.market.global.error.exception.ErrorCode;
import com.project.market.web.orderhist.dto.OrderHistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public void registerOrder(Member member, List<OrderItem> orderItemList) {
        Order order = Order.createOrder(member, orderItemList, LocalDateTime.now());

        ordersRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderItem findOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NO_MATCHING_ORDER_ITEM));
    }

    @Transactional
    public void deleteOrderItemById(Long orderItemId) {
        restoreItemStock(orderItemId);
        orderItemRepository.deleteById(orderItemId);
    }

    private void restoreItemStock(Long orderItemId) {
        OrderItem orderItem = findOrderItemById(orderItemId);
        Item item = orderItem.getItem();
        item.increaseStock(orderItem.getCount());
    }

    @Transactional
    public void changeOrderItemCount(OrderItem orderItem, int count) {
        orderItem.setCount(count);
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderHistPage(Member member, Pageable pageable) {
        return ordersRepository.getOrderHistByMember(member, pageable);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.NO_MATCHING_ORDER_ITEM));

        restoreItemStocks(order);

        order.cancel();
    }

    private void restoreItemStocks(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.stream().forEach(orderItem -> restoreItemStock(orderItem.getId()));
    }
}
