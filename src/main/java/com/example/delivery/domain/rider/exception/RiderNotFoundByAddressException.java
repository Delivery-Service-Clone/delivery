package com.example.delivery.domain.rider.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class RiderNotFoundByAddressException extends BusinessException {

  public RiderNotFoundByAddressException() {
    super(ErrorCode.RIDER_NOT_FOUND_ERROR);
  }
}
