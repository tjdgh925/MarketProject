package com.project.market.domain.order.repository;

import com.project.market.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findById(Long id);

    void deleteById(Long id);
}
