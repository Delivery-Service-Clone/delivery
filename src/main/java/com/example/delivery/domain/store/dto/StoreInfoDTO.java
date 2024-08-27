package com.example.delivery.domain.store.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreInfoDTO {

  private Long StoreId;
  private String StoreName;
  private String StoreAddress;
  private String StorePhone;

  @JsonCreator
  public StoreInfoDTO(
      @JsonProperty("storeId") Long storeId,
      @JsonProperty("storeName") String storeName,
      @JsonProperty("storeAddress") String storeAddress,
      @JsonProperty("storePhone") String storePhone) {
    this.StoreId = storeId;
    this.StoreName = storeName;
    this.StoreAddress = storeAddress;
    this.StorePhone = storePhone;
  }
}
