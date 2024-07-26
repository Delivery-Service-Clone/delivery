package com.example.delivery.domain.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class StoreDto {

  @Getter
  public static class request {

    @NotNull
    private Long ownerId;
    @NotNull
    private String storeName;
    @NotNull
    private String storePhone;
    @NotNull
    private String storeAddress;
    @NotNull
    private String openStatus;
    @NotNull
    private String introduction;
  }
}
