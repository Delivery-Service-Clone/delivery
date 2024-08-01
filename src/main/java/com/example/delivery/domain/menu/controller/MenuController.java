package com.example.delivery.domain.menu.controller;

import static com.example.delivery.global.result.ResultCode.MENU_GET_SUCCESS;
import static com.example.delivery.global.result.ResultCode.MENU_REGISTRATION_SUCCESS;

import com.example.delivery.domain.menu.dto.MenuDto;
import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PostMapping
  @Operation(summary = "메뉴 생성", description = "새로운 메뉴를 생성한다.")
  public ResponseEntity<ResultResponse> registerMenu(
      @Valid @RequestBody MenuCreateDto menuCreateDto) {
    menuService.registerMenu(menuCreateDto);
    return ResponseEntity.ok(ResultResponse.of(MENU_REGISTRATION_SUCCESS));
  }

  @GetMapping("/{storeId}")
  @Operation(summary = "메뉴 조회", description = "가게 ID를 기준으로 메뉴를 조회한다.")
  public ResponseEntity<ResultResponse> getMenusByStoreId(@PathVariable Long storeId) {
    List<MenuDto> menus = menuService.getMenusByStoreId(storeId);
    return ResponseEntity.ok(ResultResponse.of(MENU_GET_SUCCESS, menus));
  }
}
