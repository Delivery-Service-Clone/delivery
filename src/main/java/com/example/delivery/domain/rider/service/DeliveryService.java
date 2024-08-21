package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

  private final DeliveryDao deliveryDao;

  public void registerOrderWhenOrderApprove(Long orderId, OrderReceiptDto orderReceipt) {
    deliveryDao.insertStandbyOrder(orderId, orderReceipt);
  }

  public List<String> loadOrderList(String riderAddress) {
    return deliveryDao.selectOrderList(riderAddress);
  }
}
