package com.example.delivery.domain.order.service;

import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.CartItemDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartItemDAO cartItemDAO;

  public List<CartItemDTO> loadCart(String userId) {
    return cartItemDAO.selectCartList(userId);
  }

  public void registerMenuInCart(String userId, CartItemDTO cart) {
    cartItemDAO.insertMenu(userId, cart);
  }

  public void deleteAllMenuInCart(String userId) {
    cartItemDAO.deleteMenuList(userId);
  }
}