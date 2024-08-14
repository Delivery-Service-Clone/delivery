package com.example.delivery.domain.rider.controller;

import static com.example.delivery.global.result.ResultCode.RIDER_REGISTRATION_SUCCESS;
import static com.example.delivery.global.result.ResultCode.RIDER_WORK_FINISHED;
import static com.example.delivery.global.result.ResultCode.RIDER_WORK_STARTED;

import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.fcm.service.FCMService;
import com.example.delivery.domain.rider.dto.RiderCreateDto;
import com.example.delivery.domain.rider.dto.RiderDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.service.RiderService;
import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/riders")
@RequiredArgsConstructor
@Tag(name = "라이더", description = "라이더 관련 API")
public class RiderController {

  private final RiderService riderService;
  private final FCMService fcmService;

  @PostMapping
  @Operation(summary = "라이더 생성", description = "라이더를 등록한다.")
  public ResponseEntity<ResultResponse> registerRider(
      @Valid @RequestBody RiderCreateDto riderCreateDto) {
    riderService.registerRider(riderCreateDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_REGISTRATION_SUCCESS));
  }

  @GetMapping("/me")
  public ResponseEntity<ResultResponse> getMember(@AuthenticationPrincipal Rider rider) {
    if (rider == null) {
      throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
    }

    RiderDto riderDto =
        RiderDto.builder()
            .email(rider.getEmail())
            .name(rider.getName())
            .phone(rider.getPhone())
            .address(rider.getAddress())
            .build();

    return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_SUCCESS, riderDto));
  }

  // 라이더가 근무를 시작할 때 호출되는 엔드포인트
  @PostMapping("/work")
  public ResponseEntity<ResultResponse> registerStandbyRider(@RequestBody RiderDto riderDto) {
    riderService.registerStandbyRiderWhenStartWork(riderDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_WORK_STARTED));
  }

  @DeleteMapping("/finish")
  public ResponseEntity<ResultResponse> finishStandbyRider(@RequestBody RiderDto riderDto) {
    riderService.deleteStandbyRiderWhenStopWork(riderDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_WORK_FINISHED));
  }

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
