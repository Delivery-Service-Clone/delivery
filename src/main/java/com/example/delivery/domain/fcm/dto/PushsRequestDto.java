package com.example.delivery.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PushsRequestDto {
  private String title;
  private String content;
  private String address;
}
