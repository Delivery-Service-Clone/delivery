package com.example.delivery.domain.rider.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RiderCreateDto {

  private String name;

  private String phone;

  private String address;
}