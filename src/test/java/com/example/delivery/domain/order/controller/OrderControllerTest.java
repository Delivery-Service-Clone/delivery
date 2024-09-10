package com.example.delivery.domain.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.delivery.domain.order.service.OrderService;
import com.example.delivery.domain.pay.entity.PayType;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.global.result.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

  @Mock private OrderService orderService;
  @InjectMocks private OrderController orderController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("주문 등록 API 테스트")
  void registerOrder() throws Exception {
    // given
    Member member =
        Member.builder()
            .email("testmember@test.com")
            .name("테스트멤버")
            .password("password")
            .phone("010-1234-5678")
            .address("Paris")
            .build();
    long storeId = 1L;
    PayType payType = PayType.CARD;

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/food/stores/{storeId}/orders?payType={payType}", storeId, payType)
                .contentType("application/json")
                .principal(() -> member.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ORDER_REGISTRATION_SUCCESS.getCode()));
  }

  @Test
  @DisplayName("가게 주문 조회 API 테스트")
  void loadStoreOrder() throws Exception {
    // given
    Owner owner =
        Owner.builder()
            .email("testowner@test.com")
            .name("테스트오너")
            .password("password")
            .phone("010-1234-5678")
            .address("Paris")
            .build();
    long storeId = 1L;

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/food/stores/{storeId}/orders", storeId)
                .contentType("application/json")
                .principal(() -> owner.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ORDER_STORE_GET_SUCCESS.getCode()));
  }

  @Test
  @DisplayName("사용자 주문 조회 API 테스트")
  void loadOrder() throws Exception {
    // given
    Member member =
        Member.builder()
            .email("testmember@test.com")
            .name("테스트멤버")
            .password("password")
            .phone("010-1234-5678")
            .address("Paris")
            .build();
    long storeId = 1L;
    long orderId = 1L;

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/food/stores/{storeId}/orders/{orderId}", storeId, orderId)
                .contentType("application/json")
                .principal(() -> member.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ORDER_USER_GET_SUCCESS.getCode()));
  }

  @Test
  @DisplayName("주문 승인 API 테스트")
  void approvedOrder() throws Exception {
    // given
    Owner owner =
        Owner.builder()
            .email("testowner@test.com")
            .name("테스트오너")
            .password("password")
            .phone("010-1234-5678")
            .address("Paris")
            .build();
    long storeId = 1L;
    long orderId = 1L;

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/food/stores/{storeId}/orders/{orderId}", storeId, orderId)
                .contentType("application/json")
                .principal(() -> owner.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ORDER_APPROVED_SUCCESS.getCode()));
  }
}
