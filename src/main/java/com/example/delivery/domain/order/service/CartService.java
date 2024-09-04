package com.example.delivery.domain.order.service;

import com.example.delivery.domain.menu.exception.MenuNotFoundException;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.exception.CartNotFoundException;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartItemDAO cartItemDAO;
  private final StoreRepository storeRepository;
  private final MenuRepository menuRepository;

  public List<CartItemDTO> loadCart(String userId) {
    return cartItemDAO.selectCartList(userId);
  }

  public void registerMenuInCart(String userId, CartItemDTO cart) {
    if (cart == null) {
      throw new CartNotFoundException();
    }
    if (!storeRepository.existsById(cart.getStoreId())) {
      throw new StoreNotFoundException();
    }
    if (!menuRepository.existsById(cart.getMenuId())) {
      throw new MenuNotFoundException();
    }

    cartItemDAO.insertMenu(userId, cart);
  }

  public void deleteAllMenuInCart(String userId) {
    cartItemDAO.deleteMenuList(userId);
  }

  public List<CartItemDTO> makeCartListByOrderMenu(List<OrderMenu> orderMenuList, Long storeId) {
    List<CartItemDTO> cartItemDTOList = new ArrayList<>();

    for (OrderMenu orderMenu : orderMenuList) {
      CartItemDTO cartItemDTO =
          CartItemDTO.builder()
              .name(orderMenu.getMenu().getName())
              .price(orderMenu.getMenu().getPrice())
              .menuId(orderMenu.getMenu().getId())
              .storeId(storeId)
              .count(orderMenu.getCount())
              .build();

      cartItemDTOList.add(cartItemDTO);
    }
    return cartItemDTOList;
  }
}
