package com.example.delivery.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuDto {

  private Long id;
  private String name;
  private Integer price;
  private String description;
  private String photo;
}
