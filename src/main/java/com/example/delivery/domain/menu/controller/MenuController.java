package com.example.delivery.domain.menu.controller;

import static com.example.delivery.global.result.ResultCode.MENU_REGISTRATION_SUCCESS;

import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
@Tag(name = "메뉴", description = "메뉴 관련 API")
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/")
  @Operation(summary = "메뉴 생성", description = "새로운 메뉴를 생성한다.")
  public ResponseEntity<ResultResponse> registerMenu(
      @Valid @RequestBody MenuCreateDto menuCreateDto) {
    menuService.registerMenu(menuCreateDto);
    return ResponseEntity.ok(ResultResponse.of(MENU_REGISTRATION_SUCCESS));
  }
}
