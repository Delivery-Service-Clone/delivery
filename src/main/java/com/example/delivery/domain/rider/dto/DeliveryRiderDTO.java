package com.example.delivery.domain.rider.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRiderDTO {

  @NotNull private String fcmToken;
  @NotNull private String address;
}
