package com.example.delivery.domain.order.controller;

import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.order.dto.OrderStoreDetailDTO;
import com.example.delivery.domain.order.service.OrderService;
import com.example.delivery.domain.pay.entity.PayType;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/food/stores/{storeId}/orders")
@RequiredArgsConstructor
@Tag(name = "주문", description = "주문 관련 API")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @Operation(
      summary = "주문 등록",
      description = "주문을 등록한다.",
      security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> registerOrder(
      @AuthenticationPrincipal Member member, @PathVariable Long storeId, PayType payType) {
    OrderReceiptDto orderReceipt = orderService.registerOrder(member, storeId, payType);
    return ResponseEntity.ok(
        ResultResponse.of(ResultCode.ORDER_REGISTRATION_SUCCESS, orderReceipt));
  }

  @GetMapping("/{orderId}")
  @Operation(
      summary = "사용자 주문 직접 조회",
      description = "사용자가 주문을 조회한다.",
      security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> loadOrder(
      @AuthenticationPrincipal Member member,
      @PathVariable Long storeId,
      @PathVariable Long orderId) {
    OrderReceiptDto order = orderService.getOrderInfoByOrderId(member, storeId, orderId);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.ORDER_USER_GET_SUCCESS, order));
  }

  @GetMapping
  @Operation(
      summary = "가게주인 주문 직접 조회",
      description = "가게 주인이 주문들을 조회한다.",
      security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> loadStoreOrder(
      @AuthenticationPrincipal Owner owner, @PathVariable Long storeId) {
    List<OrderStoreDetailDTO> response = orderService.getStoreOrderInfoByStoreId(storeId);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.ORDER_STORE_GET_SUCCESS, response));
  }
}
