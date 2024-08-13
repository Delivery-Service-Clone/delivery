package com.example.delivery.domain.order.controller;

import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.service.CartService;
import com.example.delivery.domain.user.entity.Member;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  public void registerMenuInCart(@AuthenticationPrincipal Member member,
      @Valid @RequestBody CartItemDTO cart) {
    String memberEmail = member.getEmail();

    cartService.registerMenuInCart(memberEmail, cart);
  }

  @GetMapping
  public List<CartItemDTO> loadCart(@AuthenticationPrincipal Member member) {
    List<CartItemDTO> cartList = cartService.loadCart(member.getEmail());
    return cartList;
  }

  @DeleteMapping
  public void deleteAllMenuInCart(@AuthenticationPrincipal Member member) {
    cartService.deleteAllMenuInCart(member.getEmail());
  }
}
