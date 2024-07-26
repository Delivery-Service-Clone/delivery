package com.example.delivery.domain.store.controller;

import static com.example.delivery.global.result.ResultCode.STORE_REGISTRATION_SUCCESS;

import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @PostMapping("/stores")
  public ResponseEntity<ResultResponse> registerStore(
      @Valid @RequestBody StoreCreateDto storeCreateDto) {

    storeService.registerStore(storeCreateDto);

    return ResponseEntity.ok(ResultResponse.of(STORE_REGISTRATION_SUCCESS));
  }
}
