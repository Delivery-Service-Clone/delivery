package com.example.delivery.domain.store.dto;

import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {

  private Long storeId;
  private Long ownerId;
  private String storeName;
  private String storePhone;
  private String storeAddress;
  private StoreStatus storeStatus;
  private String introduction;
  private Category category;
}
