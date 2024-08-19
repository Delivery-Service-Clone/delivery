package com.example.delivery.domain.rider.controller;

import com.example.delivery.domain.rider.service.DeliveryService;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

  private final DeliveryService deliveryService;

  @GetMapping("/{riderAddress}")
  public ResponseEntity<ResultResponse> loadOrderList(@PathVariable String riderAddress) {
    List<String> orderList = deliveryService.loadOrderList(riderAddress);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.ORDER_LIST_LOADED_SUCCESSFULLY, orderList));
  }
}