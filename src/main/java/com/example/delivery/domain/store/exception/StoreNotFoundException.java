package com.example.delivery.domain.store.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class StoreNotFoundException extends BusinessException {

  public StoreNotFoundException() {
    super(ErrorCode.STORE_NOT_FOUND_ERROR);
  }
}
