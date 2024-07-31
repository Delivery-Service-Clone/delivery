package com.example.delivery.domain.store.controller;

import static com.example.delivery.global.result.ResultCode.CHANGE_STORESTATUS_SUCCESS;
import static com.example.delivery.global.result.ResultCode.GET_STORES_SUCCESS;
import static com.example.delivery.global.result.ResultCode.STORE_REGISTRATION_SUCCESS;

import com.example.delivery.domain.store.dto.StoreDto;
import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;
import com.example.delivery.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
@Tag(name = "가게", description = "가게 관련 api 입니다")
public class StoreController {

  private final StoreService storeService;

  @GetMapping("/list")
  @Operation(summary = "가게 조회", description = "모든 가게를 조회한다.")
  public ResponseEntity<ResultResponse> getStores() {
    System.out.println("가게 조회 시작 성공");
    List<StoreDto> stores = storeService.getAllStores();
    return ResponseEntity.ok(ResultResponse.of(GET_STORES_SUCCESS, stores));
  }

  @PostMapping
  @Operation(
      summary = "가게 등록",
      description = "새로운 가게를 등록한다.",
      security = {@SecurityRequirement(name = "jwtAuth")})
  public ResponseEntity<ResultResponse> registerStore(
      @Valid @RequestBody StoreCreateDto storeCreateDto, @AuthenticationPrincipal Owner owner) {

    if (owner == null) {
      throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
    }
    storeService.registerStore(storeCreateDto, owner);

    return ResponseEntity.ok(ResultResponse.of(STORE_REGISTRATION_SUCCESS));
  }

  @PutMapping("/{storeId}/status")
  @Operation(summary = "가게 상태 변경", description = "가게를 열 것인지 닫을 것인지 변경한다.")
  public ResponseEntity<ResultResponse> changeStoreStatus(
      @PathVariable Long storeId, @Valid @RequestBody StoreStatus storeStatus) {
    storeService.changeStoreStatus(storeId, storeStatus);
    return ResponseEntity.ok(ResultResponse.of(CHANGE_STORESTATUS_SUCCESS));
  }
}
