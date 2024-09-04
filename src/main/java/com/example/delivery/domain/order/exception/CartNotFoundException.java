package com.example.delivery.domain.order.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class CartNotFoundException extends BusinessException {

  public CartNotFoundException() {
    super(ErrorCode.CART_NOT_FOUND_ERROR);
  }
}
