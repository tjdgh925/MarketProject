package com.project.market.domain.item.entity;

import com.project.market.domain.item.constant.ItemSellStatus;
import com.project.market.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String itemName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Item(String itemName, ItemSellStatus itemSellStatus, int price, int stockNumber, String itemDetail, Member member) {
        this.itemName = itemName;
        this.itemSellStatus = itemSellStatus;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.member = member;
    }
}
