package com.project.market.web.orderhist.controller;

import com.project.market.domain.order.entity.Order;
import com.project.market.global.event.OrderCancelEvent;
import com.project.market.web.orderhist.service.OrderHistService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderhist")
public class OrderHistRestController {

    private final OrderHistService orderHistService;
    private  final ApplicationEventPublisher publisher;

    @PatchMapping("{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable Long orderId) {
        Order order = orderHistService.cancelOrder(orderId);

        OrderCancelEvent orderCancelEvent = new OrderCancelEvent(order);
        publisher.publishEvent(orderCancelEvent);


        return new ResponseEntity<>(HttpStatus.OK);
    }
}
