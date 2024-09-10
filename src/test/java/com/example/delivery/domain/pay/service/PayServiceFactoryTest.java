package com.example.delivery.domain.pay.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.delivery.domain.pay.entity.PayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PayServiceFactoryTest {

  @Mock
  private CardPayService cardPayService;

  @Mock
  private NaverPayService naverPayService;

  @InjectMocks
  private PayServiceFactory payServiceFactory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPayService_cardPayService() {
    PayService payService = payServiceFactory.getPayService(PayType.CARD);
    assertTrue(payService instanceof CardPayService);
  }

  @Test
  public void testGetPayService_naverPayService() {
    PayService payService = payServiceFactory.getPayService(PayType.NAVER_PAY);
    assertTrue(payService instanceof NaverPayService);
  }

  @Test
  public void testGetPayService_invalidType() {
    assertThrows(IllegalArgumentException.class, () -> {
      payServiceFactory.getPayService(null);
    });
  }
}
