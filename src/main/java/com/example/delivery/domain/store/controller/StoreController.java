package com.example.delivery.domain.store.controller;

import com.example.delivery.domain.store.dto.StoreDto.request;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.global.error.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @PostMapping("/stores")
  private ResponseEntity<String> add(@Valid @RequestBody request storeRequest) {

    storeService.registerStore(storeRequest);

    return ResponseEntity.successResponse("식당 등록 성공했습니다.");
  }
}
