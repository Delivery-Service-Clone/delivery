package com.example.delivery.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {
  private String email;
  private String name;
  private String phone;
  private String address;
}
