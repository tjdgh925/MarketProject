package com.project.market.domain.itemImage.entity;

import com.project.market.domain.base.BaseEntity;
import com.project.market.domain.item.entity.Item;
import com.project.market.global.config.jpa.BooleanToYNConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
public class ItemImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    @Column(length = 500)
    private String imageName;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isRepImage;

    @Column(length = 200)
    private String originalImageName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Builder
    public ItemImage(String imageName, String originalImageName, String imageUrl, Boolean isRepImage, Item item) {
        this.imageName = imageName;
        this.originalImageName = originalImageName;
        this.imageUrl = imageUrl;
        this.isRepImage = isRepImage;
        this.item = item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void updateImage(String originalFileName, String storeFileName, String imageUrl) {
        this.originalImageName = originalFileName;
        this.imageName = storeFileName;
        this.imageUrl = imageUrl;
    }

    public void refresh() {
        this.originalImageName = "";
        this.imageName = "";
        this.imageUrl = "";
        this.isRepImage = false;
    }
}