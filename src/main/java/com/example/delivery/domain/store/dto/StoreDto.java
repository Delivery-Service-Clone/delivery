package com.example.delivery.domain.store.dto;

import com.example.delivery.domain.user.entity.Owner;
import lombok.Getter;

public class StoreDto {

  @Getter
  public static class request {

    private Long ownerId;
    private String storeName;
    private String storePhone;
    private String storeAddress;
    private String openStatus;
    private String introduction;
  }
}
