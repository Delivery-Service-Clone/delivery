package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.exception.OrderNotFoundException;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderService {

  private final DeliveryDao deliveryDao;
  private final OrderRepository orderRepository;

  public void registerStandbyRiderWhenStartWork(DeliveryRiderDTO riderDto, Rider rider) {
    deliveryDao.registerRiderWhenStartWork(riderDto, rider);
  }

  public void deleteStandbyRiderWhenStopWork(DeliveryRiderDTO riderDto, Rider rider) {
    deliveryDao.deleteRider(riderDto, rider);
  }

  @Transactional
  public void acceptStandbyOrder(Long orderId, DeliveryRiderDTO riderDto, Rider rider) {

    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    order.updateOrderToDelivering(rider, OrderStatus.DELIVERING);

    orderRepository.save(order);
    deliveryDao.updateOrderToDelivering(orderId, riderDto, rider);
  }

  @Transactional
  public void finishDeliveringOrder(Long orderId, DeliveryRiderDTO riderDTO, Rider rider) {
    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    order.updateStatus(OrderStatus.COMPLETE_DELIVERY);
    orderRepository.save(order);
    deliveryDao.registerRiderWhenStartWork(riderDTO, rider);
  }
}
