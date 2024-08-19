package com.example.delivery.domain.order.service;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.OrderMenuRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.domain.user.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final CartItemDAO cartItemDAO;
  private final StoreService storeService;
  private final MenuService menuService;
  private final OrderRepository orderRepository;
  private final OrderMenuRepository orderMenuRepository;

  @Transactional
  public void registerOrder(Member member, Long storeId) {
    Order order = addUserInfo(member, storeId);
    orderRepository.save(order);

    List<CartItemDTO> cartList = cartItemDAO.selectCartList(member.getEmail());

    registerOrderMenu(cartList, order);
  }

  private void registerOrderMenu(List<CartItemDTO> cartList, Order order) {

    long totalPrice = 0;

    for (int i = 0; i < cartList.size(); i++) {
      CartItemDTO cart = cartList.get(i);
      totalPrice += cart.getPrice() * cart.getCount();

      Menu menu = menuService.getMenuByStoreIdAndMenuId(cart.getStoreId(), cart.getMenuId());

      OrderMenu orderMenu =
          OrderMenu.builder().order(order).menu(menu).count(cart.getCount()).build();

      orderMenuRepository.save(orderMenu);
    }

    order.updateTotalPrice(totalPrice);
  }

  private Order addUserInfo(Member member, Long storeId) {
    Store store = storeService.getStoreByStoreId(storeId);
    Order order =
        Order.builder()
            .member(member)
            .store(store)
            .orderStatus(OrderStatus.BEFORE_ORDER)
            .address(member.getAddress())
            .totalPrice(0L)
            .build();

    return order;
  }
}
