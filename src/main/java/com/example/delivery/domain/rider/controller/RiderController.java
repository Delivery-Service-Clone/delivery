package com.example.delivery.domain.rider.controller;

import static com.example.delivery.global.result.ResultCode.RIDER_WORK_FINISHED;
import static com.example.delivery.global.result.ResultCode.RIDER_WORK_STARTED;

import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.fcm.service.FCMService;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.dto.RiderDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.service.DeliveryService;
import com.example.delivery.domain.rider.service.RiderService;
import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
  private final DeliveryService deliveryService;
  private final FCMService fcmService;

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
  @Operation(
      summary = "라이더 출근 등록",
      description = "라이더가 출근했음을 알린다.",
      security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> registerStandbyRider(
      @RequestBody DeliveryRiderDTO riderDto, @AuthenticationPrincipal Rider rider) {
    if (rider == null) {
      throw new BusinessException(ErrorCode.RIDER_NOT_FOUND_ERROR);
    }
    riderService.registerStandbyRiderWhenStartWork(riderDto, rider);
    return ResponseEntity.ok(ResultResponse.of(RIDER_WORK_STARTED));
  }

  @DeleteMapping("/finish")
  @Operation(
      summary = "라이더 퇴근",
      description = "라이더가 퇴근했음을 알린다.",
      security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> finishStandbyRider(
      @RequestBody DeliveryRiderDTO riderDto, @AuthenticationPrincipal Rider rider) {
    if (rider == null) {
      throw new BusinessException(ErrorCode.RIDER_NOT_FOUND_ERROR);
    }
    riderService.deleteStandbyRiderWhenStopWork(riderDto, rider);
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

  @PatchMapping("/accept-order")
  @Operation(summary = "라이더 배달 수락", description = "라이더는 해당 주문을 승인한다.", security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> acceptStandbyOrder(
      @RequestParam Long orderId, @RequestBody @Valid DeliveryRiderDTO riderDto ,@AuthenticationPrincipal Rider rider) {
    if (rider == null) {
      throw new BusinessException(ErrorCode.RIDER_NOT_FOUND_ERROR);
    }
    riderService.acceptStandbyOrder(orderId, riderDto, rider);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.RIDER_DELIVERY_STARTED));
  }

  @PatchMapping("/finish-order")
  @Operation(summary = "라이더 배달 완료", description = "라이더는 배달을 완료한다.", security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> finishDeliveringOrder(
      @RequestParam Long orderId, @RequestBody @Valid DeliveryRiderDTO riderDto, @AuthenticationPrincipal Rider rider) {
    if (rider == null) {
      throw new BusinessException(ErrorCode.RIDER_NOT_FOUND_ERROR);
    }
    riderService.finishDeliveringOrder(orderId, riderDto, rider);
    return ResponseEntity.ok(ResultResponse.of(ResultCode.ORDER_DELIVERY_COMPLETED));
  }
}
