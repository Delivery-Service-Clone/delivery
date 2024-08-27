package com.example.delivery.domain.menu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuCreateDto {

  @NotNull
  private Long storeId;
  @NotNull
  private String menuName;
  @NotNull
  private Long price;
  @NotNull
  private String description;
  @NotNull
  private String photo;
}
