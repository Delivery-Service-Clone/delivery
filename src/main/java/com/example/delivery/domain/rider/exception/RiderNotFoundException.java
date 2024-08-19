package com.example.delivery.domain.rider.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class RiderNotFoundException extends BusinessException {

  public RiderNotFoundException() {
    super(ErrorCode.RIDER_NOT_FOUND_ERROR);
  }
}
