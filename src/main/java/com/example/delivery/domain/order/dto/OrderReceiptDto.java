package com.example.delivery.domain.order.dto;

import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.store.dto.StoreInfoDTO;
import com.example.delivery.domain.user.dto.MemberInfoDto;
import com.example.delivery.domain.user.entity.Member;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReceiptDto {

  private Long orderId;

  private String orderStatus;

  private Long totalPrice;

  private MemberInfoDto memberInfo;

  private StoreInfoDTO storeInfo;

  private List<CartItemDTO> cartList;

  @JsonCreator
  public OrderReceiptDto(@JsonProperty(value = "orderId") Long orderId,
      @JsonProperty(value = "orderStatus") String orderStatus,
      @JsonProperty(value = "userInfo") MemberInfoDto memberInfo,
      @JsonProperty(value = "totalPrice") Long totalPrice,
      @JsonProperty(value = "storeInfo") StoreInfoDTO storeInfo,
      @JsonProperty(value = "cartList") List<CartItemDTO> cartList) {
    this.orderId = orderId;
    this.orderStatus = orderStatus;
    this.memberInfo = memberInfo;
    this.totalPrice = totalPrice;
    this.storeInfo = storeInfo;
    this.cartList = cartList;
  }
}
