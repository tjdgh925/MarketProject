package com.project.market.global.config.listener;

import com.project.market.global.event.OrderCancelEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancelListener implements ApplicationListener<OrderCancelEvent> {

    @Override
    public void onApplicationEvent(OrderCancelEvent event) {
        System.out.println(("Order 취소 이벤트 발생, OrderId = " + event.getOrder().getId()));
    }
}
