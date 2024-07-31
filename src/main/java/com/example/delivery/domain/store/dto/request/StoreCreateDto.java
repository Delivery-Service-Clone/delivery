package com.example.delivery.domain.store.dto.request;

import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.user.entity.Owner;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreCreateDto {

  @NotNull private String storeName;
  @NotNull private String storePhone;
  @NotNull private String storeAddress;
  @NotNull private StoreStatus storeStatus;
  @NotNull private String introduction;
  @NotNull private Category category;

  public Store toEntity(StoreCreateDto storeCreateRequest, Owner owner) {

    return Store.builder()
        .owner(owner)
        .name(storeCreateRequest.getStoreName())
        .address(storeCreateRequest.getStoreAddress())
        .phone(storeCreateRequest.getStorePhone())
        .storeStatus(storeCreateRequest.getStoreStatus())
        .introduction(storeCreateRequest.getIntroduction())
        .category(storeCreateRequest.getCategory())
        .build();
  }
}
