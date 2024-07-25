package com.example.delivery.global.exception;

import com.example.delivery.global.error.ResponseType;

public class AuthException extends BaseException {

  public AuthException(ResponseType responseType) {
    super(responseType);
  }
}
