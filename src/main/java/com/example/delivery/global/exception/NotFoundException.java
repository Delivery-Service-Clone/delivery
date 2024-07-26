package com.example.delivery.global.exception;

import com.example.delivery.global.error.ResponseType;

public class NotFoundException extends BaseException {

  public NotFoundException(ResponseType responseType) {
    super(responseType);
  }
}
