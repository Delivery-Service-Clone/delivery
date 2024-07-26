package com.example.delivery.domain.menu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuCreateDto {

  @NotNull
  private Long storeId;
  @NotNull
  private String menuName;
  @NotNull
  private Integer price;
  @NotNull
  private String description;
  @NotNull
  private String photo;
}
