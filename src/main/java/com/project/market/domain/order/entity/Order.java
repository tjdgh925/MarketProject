package com.project.market.domain.order.entity;

import com.project.market.domain.base.BaseEntity;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.order.constant.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(OrderStatus orderStatus, LocalDateTime orderTime, Member member) {
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.member = member;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList, LocalDateTime orderTime) {
        Order order = Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .orderTime(orderTime)
                .build();
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        orderItem.setCart(null);
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCEL;
    }
}

