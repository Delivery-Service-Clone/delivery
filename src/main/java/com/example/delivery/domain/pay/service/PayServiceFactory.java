package com.example.delivery.domain.pay.service;

import com.example.delivery.domain.pay.entity.PayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/*
 - 팩토리 방식을 선택한 이유
   컨트롤러에서 어떤 결제수단이냐에 따라 switch문을 통해 결제 서비스를 주입해주면 비즈니스 로직이 컨트롤러에
   혼재되어 있는 상황이 발생. 결제 서비스를 주입해주는 정도니까 서비스 단에 비즈니스로직을 수행하라고 넘기는
   컨트롤러의 역할로 볼 수도 있으나 컨트롤러에서 모든 결제서비스의 빈을 가지고 있어야 하고, 어떤 결제수단인지
   를 정하는 것 자체가 비즈니스 로직이라고 판단하여 컨트롤러에서 결제서비스 주입하는 방식을 선택하지 않음.
   출처 : F-Lab make-delivery 결제 생성 추가 PR 중
*/

@Component
@RequiredArgsConstructor
public class PayServiceFactory {

  private final CardPayService cardPayService;
  private final NaverPayService naverPayService;

  public PayService getPayService(PayType payType) {

    PayService payService = null;

    switch (payType) {
      case CARD:
        payService = cardPayService;
        break;
      case NAVER_PAY:
        payService = naverPayService;
        break;
      default:
        throw new IllegalArgumentException();
    }
    return payService;
  }
}
