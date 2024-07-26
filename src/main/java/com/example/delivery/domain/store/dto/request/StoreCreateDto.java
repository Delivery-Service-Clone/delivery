package com.example.delivery.domain.store.dto.request;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.Owner;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreCreateDto {

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

  public Store toEntity(StoreCreateDto storeCreateRequest, Owner owner) {

    return Store.builder()
        .owner(owner)
        .name(storeCreateRequest.getStoreName())
        .address(storeCreateRequest.getStoreAddress())
        .phone(storeCreateRequest.getStorePhone())
        .openStatus(storeCreateRequest.getOpenStatus())
        .introduction(storeCreateRequest.getIntroduction())
        .build();
  }
}