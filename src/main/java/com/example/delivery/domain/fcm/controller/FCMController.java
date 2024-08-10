package com.example.delivery.domain.fcm.controller;

import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.fcm.service.FCMService;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
@Tag(name = "FCM", description = "FCM 관련 API")
public class FCMController {

  private final FCMService fcmService;

  @PostMapping("/send")
  @Operation(summary = "라이더에 알림 요청", description = "해당 지역 내의 라이더에게 알림 요청을 한다.")
  public ResponseEntity<ResultResponse> sendPushNotification(
      @RequestBody PushsRequestDto pushsRequestDto) {
    try {
      fcmService.sendPushs(pushsRequestDto);
      return ResponseEntity.ok(ResultResponse.of(ResultCode.FCM_SEND_SUCCESS));
    } catch (FirebaseMessagingException e) {
      return ResponseEntity.ok(ResultResponse.of(ResultCode.FCM_SEND_FAIL));
    }
  }
}
