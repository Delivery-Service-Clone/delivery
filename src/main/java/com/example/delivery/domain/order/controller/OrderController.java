package com.example.delivery.domain.order.controller;

import com.example.delivery.domain.order.service.OrderService;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/food/stores/{storeId}/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<ResultResponse> registerOrder(
      @AuthenticationPrincipal Member member, @PathVariable("storeId") Long storeId) {
    orderService.registerOrder(member, storeId);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.ORDER_REGISTRATION_SUCCESS));
  }
}
