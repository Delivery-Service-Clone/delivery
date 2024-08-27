package com.example.delivery.domain.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class StoreInfoDTO {

  private final Long StoreId;
  private final String StoreName;
  private final String StoreAddress;
  private final String StorePhone;
}
