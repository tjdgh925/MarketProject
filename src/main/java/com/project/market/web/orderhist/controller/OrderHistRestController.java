package com.project.market.web.orderhist.controller;

import com.project.market.web.orderhist.service.OrderHistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderhist")
public class OrderHistRestController {

    private final OrderHistService orderHistService;

    @PatchMapping("{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable Long orderId) {
        orderHistService.cancelOrder(orderId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
