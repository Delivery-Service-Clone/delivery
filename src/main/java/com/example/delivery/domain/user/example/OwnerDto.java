package com.example.delivery.domain.user.example;

import lombok.Getter;

public class OwnerDto {

  @Getter
  public static class Request {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
  }
}
