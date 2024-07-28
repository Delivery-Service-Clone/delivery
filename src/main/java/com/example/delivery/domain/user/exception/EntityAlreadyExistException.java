package com.example.delivery.domain.user.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class EntityAlreadyExistException extends BusinessException {

  public EntityAlreadyExistException() {
    super(ErrorCode.ALREADY_EXIST_USER);
  }
}
