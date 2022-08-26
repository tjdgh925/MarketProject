package com.project.market.global.event;

import com.project.market.domain.order.entity.Order;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class OrderCancelEvent extends ApplicationEvent {

    private final Order order;

    public OrderCancelEvent(Object source) {
        super(source);
        this.order = (Order) source;
    }

    public Order getOrder() {
        return order;
    }
}
