package com.example.delivery.domain.rider.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryRiderDTO {

  @NotNull private String id;
  @NotNull private String fcmToken;
  @NotNull private String name;
  @NotNull private String address;
}
