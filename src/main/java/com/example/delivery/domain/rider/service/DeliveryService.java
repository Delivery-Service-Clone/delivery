package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.repository.RiderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

  private final DeliveryDao deliveryDao;

  public void registerOrderWhenOrderApprove(long orderId, OrderReceiptDto orderReceipt) {
    deliveryDao.insertStandbyOrder(orderId, orderReceipt);
  }

  public List<String> loadOrderList(String riderAddress) {
    return deliveryDao.selectOrderList(riderAddress);
  }
}
