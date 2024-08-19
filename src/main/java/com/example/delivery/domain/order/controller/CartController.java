package com.example.delivery.domain.order.controller;

import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.service.CartService;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<ResultResponse> registerMenuInCart(
      @AuthenticationPrincipal Member member, @Valid @RequestBody CartItemDTO cart) {
    String memberEmail = member.getEmail();

    cartService.registerMenuInCart(memberEmail, cart);

    return ResponseEntity.ok(ResultResponse.of(ResultCode.MENU_IN_CART_SUCCESS));
  }

  @GetMapping
  public ResponseEntity<ResultResponse> loadCart(@AuthenticationPrincipal Member member) {
    List<CartItemDTO> cartList = cartService.loadCart(member.getEmail());
    return ResponseEntity.ok(ResultResponse.of(ResultCode.CART_GET_SUCCESS, cartList));
  }

  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteAllMenuInCart(
      @AuthenticationPrincipal Member member) {
    cartService.deleteAllMenuInCart(member.getEmail());
    return ResponseEntity.ok(ResultResponse.of(ResultCode.CART_DELETE_SUCCESS));
  }
}
