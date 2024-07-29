package com.example.delivery.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {

  private String email;
  private String name;
  private String phone;
  private String address;

  @Builder
  public MemberDto(String email, String name, String phone, String address) {
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.address = address;
  }
}
