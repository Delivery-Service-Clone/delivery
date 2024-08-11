package com.example.delivery.domain.rider.controller;

import static com.example.delivery.global.result.ResultCode.RIDER_DELIVERY_STARTED;
import static com.example.delivery.global.result.ResultCode.RIDER_REGISTRATION_SUCCESS;
import static com.example.delivery.global.result.ResultCode.RIDER_WORK_FINISHED;
import static com.example.delivery.global.result.ResultCode.RIDER_WORK_STARTED;

import com.example.delivery.domain.rider.dto.RiderCreateDto;
import com.example.delivery.domain.rider.dto.RiderDto;
import com.example.delivery.domain.rider.service.RiderService;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/riders")
@RequiredArgsConstructor
@Tag(name = "라이더", description = "라이더 관련 API")
public class RiderController {

  private final RiderService riderService;

  @PostMapping
  @Operation(summary = "라이더 생성", description = "라이더를 등록한다.")
  public ResponseEntity<ResultResponse> registerRider(
      @Valid @RequestBody RiderCreateDto riderCreateDto) {
    riderService.registerRider(riderCreateDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_REGISTRATION_SUCCESS));
  }

  // 라이더가 근무를 시작할 때 호출되는 엔드포인트
  @PostMapping("/work")
  public ResponseEntity<ResultResponse> registerStandbyRider(@RequestBody RiderDto riderDto) {
    riderService.registerStandbyRiderWhenStartWork(riderDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_WORK_STARTED));
  }

  // 라이더가 배달을 시작할 때 호출되는 엔드포인트
  @PutMapping("/{riderId}/delivery")
  public ResponseEntity<ResultResponse> updateRiderStatusToDelivering(
      @PathVariable Long riderId,
      @RequestParam String address) {
    riderService.updateRiderStatusToDelivering(riderId, address);
    return ResponseEntity.ok(ResultResponse.of(RIDER_DELIVERY_STARTED));
  }

  @DeleteMapping("/finish")
  public ResponseEntity<ResultResponse> finishStandbyRider(@RequestBody RiderDto riderDto) {
    riderService.deleteStandbyRiderWhenStopWork(riderDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_WORK_FINISHED));
  }
}
