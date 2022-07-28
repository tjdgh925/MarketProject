package com.project.market.domain.item.repository;

import com.project.market.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Override
    Optional<Item> findById(Long id);
}
