package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.exception.OrderNotFoundException;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.exception.RiderNotFoundException;
import com.example.delivery.domain.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderService {

  private final RiderRepository riderRepository;
  private final DeliveryDao deliveryDao;
  private final OrderRepository orderRepository;

  public void registerStandbyRiderWhenStartWork(DeliveryRiderDTO riderDto) {
    deliveryDao.registerRiderWhenStartWork(riderDto);
  }

  public void deleteStandbyRiderWhenStopWork(DeliveryRiderDTO riderDto) {
    deliveryDao.deleteRider(riderDto);
  }

  @Transactional
  public void acceptStandbyOrder(Long orderId, DeliveryRiderDTO riderDto) {
    Rider rider =
        riderRepository
            .findById(Long.parseLong(riderDto.getId()))
            .orElseThrow(RiderNotFoundException::new);
    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    order.updateOrderToDelivering(rider, OrderStatus.DELIVERING);
    orderRepository.save(order);
    deliveryDao.updateOrderToDelivering(orderId, riderDto);
  }

  @Transactional
  public void finishDeliveringOrder(Long orderId, DeliveryRiderDTO rider) {
    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    order.updateStatus(OrderStatus.COMPLETE_DELIVERY);
    orderRepository.save(order);
    deliveryDao.registerRiderWhenStartWork(rider);
  }
}
