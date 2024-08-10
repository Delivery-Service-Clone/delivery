package com.example.delivery.domain.rider.controller;

import static com.example.delivery.global.result.ResultCode.MENU_REGISTRATION_SUCCESS;
import static com.example.delivery.global.result.ResultCode.RIDER_REGISTRATION_SUCCESS;

import com.example.delivery.domain.rider.dto.RiderCreateDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.service.RiderService;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/riders")
@RequiredArgsConstructor
@Tag(name = "라이더", description = "라이더 관련 API")
public class RiderController {

  private final RiderService riderService;

  @PostMapping
  @Operation(summary = "라이더 생성", description = "라이더를 등록한다.")
  public ResponseEntity<ResultResponse> registerRider(@Valid @RequestBody RiderCreateDto riderCreateDto) {
    riderService.registerRider(riderCreateDto);
    return ResponseEntity.ok(ResultResponse.of(RIDER_REGISTRATION_SUCCESS));
  }
}