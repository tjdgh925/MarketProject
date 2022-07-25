package com.project.market.domain.itemImage.repository;

import com.project.market.domain.item.entity.Item;
import com.project.market.domain.itemImage.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemOrderById(Item item);
}
