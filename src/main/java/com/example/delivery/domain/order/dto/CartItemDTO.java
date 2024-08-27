package com.example.delivery.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemDTO {

  @NotNull private String name;

  @NotNull private Long price;

  @NotNull private Long menuId;

  @NotNull private Long storeId;

  @NotNull private Long count;

  @JsonCreator
  public CartItemDTO(
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "price") Long price,
      @JsonProperty(value = "menuId") Long menuId,
      @JsonProperty(value = "storeId") Long storeId,
      @JsonProperty(value = "count") Long count) {
    this.name = name;
    this.price = price;
    this.menuId = menuId;
    this.storeId = storeId;
    this.count = count;
  }

  public CartItemDTO(Long menuId, String name, Long price, Long count) {
    this.menuId = menuId;
    this.name = name;
    this.price = price;
    this.count = count;
  }
}
