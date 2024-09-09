package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.order.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByStoreId(Long storeId);

  Optional<Order> findByStoreIdAndId(Long storeId, Long orderId);
}
