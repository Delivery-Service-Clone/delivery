package com.example.delivery.domain.fcm.controller;

import com.example.delivery.domain.fcm.dto.PushRequestDto;
import com.example.delivery.domain.fcm.service.FCMService;
import com.example.delivery.domain.fcm.service.RabbitMQSender;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {

  private final FCMService fcmService;
  private final RabbitMQSender rabbitMQSender;

  @PostMapping("/send")
  public ResponseEntity<ResultResponse> sendPushNotification(
      @RequestBody PushRequestDto pushRequestDto) throws FirebaseMessagingException {
    rabbitMQSender.send(pushRequestDto);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.FCM_SEND_SUCCESS, ""));
  }
}
