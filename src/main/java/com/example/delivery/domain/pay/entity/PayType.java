package com.example.delivery.domain.pay.entity;

public enum PayType {
  CARD("카드"),
  NAVER_PAY("네이버 페이");

  private String payType;

  PayType(String payType) {
    this.payType = payType;
  }

  public String getPayType() {
    return this.payType;
  }
}
