package com.example.delivery.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PushsRequestDto {
  private String title;
  private String content;
  private String address;
}
