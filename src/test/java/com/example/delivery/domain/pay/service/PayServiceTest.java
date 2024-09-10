package com.example.delivery.domain.pay.service;

import static org.mockito.Mockito.*;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.pay.entity.Pay;
import com.example.delivery.domain.pay.entity.PayStatus;
import com.example.delivery.domain.pay.entity.PayType;
import com.example.delivery.domain.pay.repository.PayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PayServiceTest {

  @Mock private PayRepository payRepository;

  @Mock private Order order; // 실제 Order 대신 Mock을 사용

  @InjectMocks private CardPayService cardPayService;

  @InjectMocks private NaverPayService naverPayService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCardPayService_pay() {
    long price = 1000L;

    // CardPayService의 pay 메서드 호출
    cardPayService.pay(price, order);

    // PayRepository의 save 메서드가 호출되었는지 확인
    verify(payRepository, times(1)).save(any(Pay.class));

    // pay 객체가 올바르게 생성되고, 해당 값들이 설정되었는지 검증
    verify(payRepository)
        .save(
            argThat(
                pay ->
                    pay.getPayType() == PayType.CARD
                        && pay.getPrice() == price
                        && pay.getOrder().equals(order)
                        && pay.getStatus() == PayStatus.COMPLETE_PAY));
  }

  @Test
  public void testNaverPayService_pay() {
    long price = 2000L;

    // NaverPayService의 pay 메서드 호출
    naverPayService.pay(price, order);

    // PayRepository의 save 메서드가 호출되었는지 확인
    verify(payRepository, times(1)).save(any(Pay.class));

    // pay 객체가 올바르게 생성되고, 해당 값들이 설정되었는지 검증
    verify(payRepository)
        .save(
            argThat(
                pay ->
                    pay.getPayType() == PayType.NAVER_PAY
                        && pay.getPrice() == price
                        && pay.getOrder().equals(order)
                        && pay.getStatus() == PayStatus.COMPLETE_PAY));
  }
}
