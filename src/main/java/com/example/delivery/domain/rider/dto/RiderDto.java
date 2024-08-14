package com.example.delivery.domain.rider.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RiderDto {

  private String email;
  private String name;
  private String phone;
  private String address;

  @Builder
  public RiderDto(String email, String name, String phone, String address) {
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.address = address;
  }
}
