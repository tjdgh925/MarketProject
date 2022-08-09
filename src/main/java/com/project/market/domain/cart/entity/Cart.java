package com.project.market.domain.cart.entity;

import com.project.market.domain.base.BaseEntity;
import com.project.market.domain.member.entity.Member;
import com.project.market.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;

    @Builder
    public Cart(Member member, List<OrderItem> orderItemList) {
        this.member = member;
        this.orderItemList = orderItemList;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItemList.add(orderItem);
        orderItem.setCart(this);
    }
}
