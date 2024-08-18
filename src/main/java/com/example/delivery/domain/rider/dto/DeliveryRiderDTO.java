package com.example.delivery.domain.rider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRiderDTO {

  private String id;
  private String fcmToken;
  private String name;
  private String address;
}
