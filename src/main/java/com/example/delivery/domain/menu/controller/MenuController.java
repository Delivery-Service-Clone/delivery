package com.example.delivery.domain.menu.controller;

import static com.example.delivery.global.result.ResultCode.MENU_REGISTRATION_SUCCESS;

import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/menus")
  public ResponseEntity<ResultResponse> registerMenu(
      @Valid @RequestBody MenuCreateDto menuCreateDto) {
    menuService.registerMenu(menuCreateDto);
    return ResponseEntity.ok(ResultResponse.of(MENU_REGISTRATION_SUCCESS));
  }
}
