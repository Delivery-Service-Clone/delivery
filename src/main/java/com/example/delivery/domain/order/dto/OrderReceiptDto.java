package com.example.delivery.domain.order.dto;

import com.example.delivery.domain.user.dto.MemberInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderReceiptDto {

  private Long orderId;

  private String orderStatus;

  private Long totalPrice;

  private MemberInfoDto userInfo;
}
