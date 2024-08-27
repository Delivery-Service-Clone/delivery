package com.example.delivery.domain.order.dto;

import com.example.delivery.domain.user.dto.MemberInfoDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderStoreDetailDTO {

  private final Long orderId;

  private final LocalDateTime orderCreatedAt;

  private final String orderStatus;

  private final Long totalPrice;

  private MemberInfoDto memberInfo;

  private List<CartItemDTO> cartList;
}
