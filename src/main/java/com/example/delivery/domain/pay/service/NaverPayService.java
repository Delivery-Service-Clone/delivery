package com.example.delivery.domain.pay.service;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.pay.entity.Pay;
import com.example.delivery.domain.pay.entity.PayStatus;
import com.example.delivery.domain.pay.entity.PayType;
import com.example.delivery.domain.pay.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverPayService implements PayService {

  private final PayRepository payRepository;

  @Override
  public void pay(long price, Order order) {
    Pay pay =
        Pay.builder()
            .payType(PayType.NAVER_PAY)
            .price(price)
            .order(order)
            .status(PayStatus.COMPLETE_PAY)
            .build();

    payRepository.save(pay);
  }
}
