package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.order.entity.OrderMenu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

  List<OrderMenu> findByOrderId(Long orderId);
}
