package com.example.delivery.domain.store.controller;

import static com.example.delivery.global.result.ResultCode.STORE_REGISTRATION_SUCCESS;

import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "가게", description = "가게 관련 api 입니다")
public class StoreController {

  private final StoreService storeService;

  @PostMapping("/stores")
  @Operation(summary = "가게 등록", description = "새로운 가게를 등록한다.")
  public ResponseEntity<ResultResponse> registerStore(
      @Valid @RequestBody StoreCreateDto storeCreateDto) {

    storeService.registerStore(storeCreateDto);

    return ResponseEntity.ok(ResultResponse.of(STORE_REGISTRATION_SUCCESS));
  }
}
