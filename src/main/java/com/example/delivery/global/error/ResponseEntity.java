package com.example.delivery.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseEntity<T> {

  String responseCode;
  String message;
  T data;

  public ResponseEntity() {
  }

  public ResponseEntity(String responseCode, String message, T data) {
    this.responseCode = responseCode;
    this.message = message;
    this.data = data;
  }

  public static <T> ResponseEntity<T> successResponse() {
    ResponseType type = ResponseType.SUCCESS;
    return new ResponseEntity<>(type.getCode(), type.getMessage(), null);
  }

  public static <T> ResponseEntity<T> successResponse(String message) {
    ResponseType type = ResponseType.SUCCESS;
    return new ResponseEntity<>(type.getCode(), message, null);
  }

  public static <T> ResponseEntity<T> successResponse(T data) {
    ResponseType type = ResponseType.SUCCESS;
    return new ResponseEntity<>(type.getCode(), type.getMessage(), data);
  }

  public static <T> ResponseEntity<T> successResponse(String message, T data) {
    ResponseType type = ResponseType.SUCCESS;
    return new ResponseEntity<>(type.getCode(), message, data);
  }

  public static <T> ResponseEntity<T> successResponse(ResponseType type, T data) {
    return new ResponseEntity<>(type.getCode(), type.getMessage(), data);
  }

  public static <T> ResponseEntity<T> failureResponse(ResponseType type) {
    return new ResponseEntity<>(type.getCode(), type.getMessage(), null);
  }

  public static <T> ResponseEntity<T> failureResponse(ResponseType type, String message) {
    return new ResponseEntity<>(type.getCode(), message, null);
  }

  public static <T> ResponseEntity<T> failureResponse(String responseCode, String message) {
    return new ResponseEntity<>(responseCode, message, null);
  }

}
