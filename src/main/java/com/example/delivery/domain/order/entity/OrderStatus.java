package com.example.delivery.domain.order.entity;

public enum OrderStatus {
  BEFORE_ORDER("주문 전"),
  COMPLETE_ORDER("주문 중"),
  APPROVED_ORDER("주문 승인됨"),
  DELIVERING("배달 중"),
  COMPLETE_DELIVERY("배달 완료");

  private String orderStatus;

  OrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getOrderStatus() {
    return this.orderStatus;
  }
}
