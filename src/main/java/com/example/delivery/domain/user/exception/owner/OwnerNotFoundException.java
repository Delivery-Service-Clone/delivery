package com.example.delivery.domain.user.exception.owner;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class OwnerNotFoundException extends BusinessException {

  public OwnerNotFoundException() {
    super(ErrorCode.OWNER_NOT_FOUND_ERROR);
  }
}
