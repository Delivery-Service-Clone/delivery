package com.example.delivery.domain.pay.entity;

public enum PayStatus {
  BEFORE_PAY("결제 전"),
  COMPLETE_PAY("결제 완료");

  private String payStatus;

  PayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

  public String getPayStatus() {
    return this.payStatus;
  }
}
