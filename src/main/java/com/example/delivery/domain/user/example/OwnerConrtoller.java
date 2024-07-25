package com.example.delivery.domain.user.example;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OwnerConrtoller {

  private final OwnerService ownerService;

  @PostMapping("/owners")
  private ResponseEntity<String> add(
      HttpSession session, @Validated @RequestBody OwnerDto.Request userRequestDto) {

    ownerService.registerUser(userRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("Owner registered successfully");
  }
}
