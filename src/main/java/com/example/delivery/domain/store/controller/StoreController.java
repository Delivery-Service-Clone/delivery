package com.example.delivery.domain.store.controller;

import com.example.delivery.domain.store.dto.StoreDto;
import com.example.delivery.domain.store.service.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @PostMapping("/stores")
  private ResponseEntity<String> add(
      HttpSession session, @Validated @RequestBody StoreDto.request storeRequest) {

    storeService.registerStore(storeRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body("Store registered successfully");
  }
}
