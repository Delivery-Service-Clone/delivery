package com.example.delivery.global.exception;

import com.example.delivery.global.error.ResponseType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final ResponseType responseType;

  public BaseException(ResponseType responseType) {
    this.responseType = responseType;
  }
}
