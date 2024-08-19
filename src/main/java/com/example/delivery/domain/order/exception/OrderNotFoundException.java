package com.example.delivery.domain.order.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class OrderNotFoundException extends BusinessException {

  public OrderNotFoundException() {
    super(ErrorCode.ORDER_NOT_FOUND_ERROR);
  }
}
