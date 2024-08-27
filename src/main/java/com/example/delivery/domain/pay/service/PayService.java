package com.example.delivery.domain.pay.service;

import com.example.delivery.domain.order.entity.Order;

public interface PayService {

  void pay(long price, Order order);
}
