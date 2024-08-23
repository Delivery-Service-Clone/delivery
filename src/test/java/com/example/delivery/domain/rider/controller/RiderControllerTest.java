package com.example.delivery.domain.rider.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.fcm.service.FCMService;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.service.RiderService;
import com.example.delivery.global.result.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RiderControllerTest {

  @Mock private RiderService riderService;

  @Mock private FCMService fcmService;

  @InjectMocks private RiderController riderController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(riderController).build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("라이더 출근 등록이 성공적으로 이루어진다")
  void registerStandbyRider() throws Exception {
    // Given
    Rider rider = Rider.builder().name("John Doe").build();
    ReflectionTestUtils.setField(rider, "id", 1L);
    DeliveryRiderDTO riderDto = new DeliveryRiderDTO("fcmToken123", "고등동");

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/riders/work")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(riderDto))
                .principal(() -> String.valueOf(rider.getId()))) // AuthenticationPrincipal mock
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.RIDER_WORK_STARTED.getCode()));
  }

  @Test
  @DisplayName("라이더 퇴근이 성공적으로 처리된다")
  void finishStandbyRider() throws Exception {
    // Given
    Rider rider = Rider.builder().name("John Doe").build();
    ReflectionTestUtils.setField(rider, "id", 1L);
    DeliveryRiderDTO riderDto = new DeliveryRiderDTO("fcmToken123", "고등동");

    // When & Then
    mockMvc
        .perform(
            delete("/api/v1/riders/finish")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(riderDto))
                .principal(() -> String.valueOf(rider.getId()))) // AuthenticationPrincipal mock
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.RIDER_WORK_FINISHED.getCode()));
  }

  @Test
  @DisplayName("라이더가 배달을 수락한다")
  void acceptStandbyOrder() throws Exception {
    // Given
    Rider rider = Rider.builder().name("John Doe").build();
    ReflectionTestUtils.setField(rider, "id", 1L);
    DeliveryRiderDTO riderDto = new DeliveryRiderDTO("fcmToken123", "고등동");
    Long orderId = 1L;

    // When & Then
    mockMvc
        .perform(
            patch("/api/v1/riders/accept-order")
                .param("orderId", String.valueOf(orderId))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(riderDto))
                .principal(() -> String.valueOf(rider.getId()))) // AuthenticationPrincipal mock
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.RIDER_DELIVERY_STARTED.getCode()));
  }

  @Test
  @DisplayName("라이더가 배달을 완료한다")
  void finishDeliveringOrder() throws Exception {
    // Given
    Rider rider = Rider.builder().name("John Doe").build();
    ReflectionTestUtils.setField(rider, "id", 1L);
    DeliveryRiderDTO riderDto = new DeliveryRiderDTO("fcmToken123", "고등동");
    Long orderId = 1L;

    // When & Then
    mockMvc
        .perform(
            patch("/api/v1/riders/finish-order")
                .param("orderId", String.valueOf(orderId))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(riderDto))
                .principal(() -> String.valueOf(rider.getId()))) // AuthenticationPrincipal mock
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ORDER_DELIVERY_COMPLETED.getCode()));
  }

  @Test
  @DisplayName("라이더에게 알림을 성공적으로 보낸다")
  void sendPushNotification() throws Exception {
    // Given
    PushsRequestDto pushsRequestDto = new PushsRequestDto();
    ReflectionTestUtils.setField(pushsRequestDto, "title", "Test Title");
    ReflectionTestUtils.setField(pushsRequestDto, "content", "Test Body");
    ReflectionTestUtils.setField(pushsRequestDto, "address", "Test Address");

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/riders/send")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(pushsRequestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.FCM_SEND_SUCCESS.getCode()));
  }
}