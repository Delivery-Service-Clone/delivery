package com.example.delivery.domain.rider.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.example.delivery.domain.store.dto.StoreInfoDTO;
import com.example.delivery.domain.user.dto.MemberInfoDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

  @Mock private DeliveryDao deliveryDao;

  @InjectMocks private DeliveryService deliveryService;

  @Test
  @DisplayName("주문 승인 시 StandbyOrder에 올바르게 등록되는지 검증")
  void registerOrderWhenOrderApprove_shouldCallInsertStandbyOrder() {
    Long orderId = 1L;
    MemberInfoDto memberInfo = new MemberInfoDto();
    StoreInfoDTO storeInfo = new StoreInfoDTO(1L, "치킨", "고등동", "02-0000-0000");
    List<CartItemDTO> cartList = new ArrayList<>();

    OrderReceiptDto orderReceipt =
        OrderReceiptDto.builder()
            .orderId(orderId)
            .orderStatus("APPROVED")
            .memberInfo(memberInfo)
            .storeInfo(storeInfo)
            .totalPrice(10000L)
            .cartList(cartList)
            .build();

    deliveryService.registerOrderWhenOrderApprove(orderId, orderReceipt);

    verify(deliveryDao).insertStandbyOrder(orderId, orderReceipt);
  }

  @Test
  @DisplayName("라이더의 주소에 따라 올바른 주문 목록을 반환하는지 검증")
  void loadOrderList_shouldReturnOrderList() {
    String riderAddress = "고등동";
    List<String> expectedOrderList = List.of("Order1", "Order2", "Order3");

    when(deliveryDao.selectOrderList(riderAddress)).thenReturn(expectedOrderList);

    List<String> actualOrderList = deliveryService.loadOrderList(riderAddress);

    assertEquals(expectedOrderList, actualOrderList);
  }
}
