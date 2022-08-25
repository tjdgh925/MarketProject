package com.project.market.domain.order.entity;

import com.project.market.domain.base.BaseEntity;
import com.project.market.domain.cart.entity.Cart;
import com.project.market.domain.item.entity.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id",  nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    public OrderItem(int count, int orderPrice, Item item) {
        this.count = count;
        this.orderPrice = orderPrice;
        this.item = item;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public void restoreItemStock() {
        Item item = this.getItem();
        item.increaseStock(this.getCount());
    }
}
