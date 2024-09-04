package com.example.delivery.domain.rider.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.delivery.domain.rider.service.DeliveryService;
import com.example.delivery.global.result.ResultCode;
import java.util.List;
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
class DeliveryControllerTest {

  @Mock private DeliveryService deliveryService;

  @InjectMocks private DeliveryController deliveryController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
  }

  @Test
  @DisplayName("라이더의 주소로 주문 목록을 성공적으로 불러온다")
  void loadOrderList_shouldReturnOrderListSuccessfully() throws Exception {
    // Given
    String riderAddress = "고등동";
    List<String> orderList = List.of("Order1", "Order2", "Order3");

    // When
    when(deliveryService.loadOrderList(riderAddress)).thenReturn(orderList);

    // Then
    mockMvc
        .perform(get("/api/v1/delivery/{riderAddress}", riderAddress))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ORDER_LIST_LOADED_SUCCESSFULLY.getCode()))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0]").value("Order1"))
        .andExpect(jsonPath("$.data[1]").value("Order2"))
        .andExpect(jsonPath("$.data[2]").value("Order3"));
  }
}
