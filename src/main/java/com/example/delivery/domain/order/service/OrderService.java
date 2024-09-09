package com.example.delivery.domain.order.service;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.order.dto.OrderStoreDetailDTO;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.exception.OrderNotFoundException;
import com.example.delivery.domain.order.repository.OrderMenuRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.pay.entity.PayType;
import com.example.delivery.domain.pay.service.PayService;
import com.example.delivery.domain.pay.service.PayServiceFactory;
import com.example.delivery.domain.store.dto.StoreInfoDTO;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.domain.user.dto.MemberInfoDto;
import com.example.delivery.domain.user.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final CartItemDAO cartItemDAO;
  private final StoreService storeService;
  private final MenuService menuService;
  private final CartService cartService;
  private final OrderRepository orderRepository;
  private final OrderMenuRepository orderMenuRepository;
  private final PayServiceFactory payServiceFactory;

  @Transactional
  public OrderReceiptDto registerOrder(Member member, Long storeId, PayType payType) {
    Order order = addUserInfo(member, storeId);
    OrderReceiptDto orderReceipt;
    orderRepository.save(order);

    List<CartItemDTO> cartList = cartItemDAO.getCartAndDelete(member.getEmail());

    restoreCartListOnOrderRollback(member.getEmail(), cartList);

    long totalPrice = registerOrderMenu(cartList, order);
    pay(payType, totalPrice, order);

    order.updateStatus(OrderStatus.COMPLETE_ORDER);

    orderReceipt = getOrderReceipt(order, cartList, totalPrice, storeId, member);

    return orderReceipt;
  }

  /*
    restoreCartListOnOrderRollback 메서드 자체는 registerOrder 메서드 내에서 호출되어 항상 실행되지만,
    이 메서드 내에 정의된 TransactionSynchronizationAdapter의 afterCompletion 메서드는 특정 조건 하에서만 실행됩니다.
    이 구현에 따르면, 트랜잭션이 롤백(되돌려진 상태)으로 완료되었을 때만 cartItemDAO.insertMenuList(userId, cartList); 코드가 실행됩니다.
    즉, 트랜잭션이 성공적으로 커밋되어 정상적으로 완료된 경우, afterCompletion 메서드 내의 if 블록은 실행되지 않습니다.
    따라서 cartItemDAO.insertMenuList(userId, cartList); 메서드 호출은 트랜잭션이 롤백될 때만 발생하며, 트랜잭션이 성공적으로 완료되면 해당 로직은 실행되지 않습니다.
  */

  private void restoreCartListOnOrderRollback(String userId, List<CartItemDTO> cartList) {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronizationAdapter() {
          @Override
          public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
              cartItemDAO.insertMenuList(userId, cartList);
            }
          }
        });
  }

  private Long registerOrderMenu(List<CartItemDTO> cartList, Order order) {

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

    return totalPrice;
  }

  @Transactional
  public void pay(PayType payType, long totalPrice, Order order) {
    PayService payService = payServiceFactory.getPayService(payType);
    payService.pay(totalPrice, order);
  }

  public OrderReceiptDto getOrderInfoByOrderId(Member member, Long storeId, Long orderId) {

    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    StoreInfoDTO storeInfo = storeService.getStoreInfo(storeId);

    List<OrderMenu> orderMenuList = orderMenuRepository.findByOrderId(orderId);
    List<CartItemDTO> cartList = cartService.makeCartListByOrderMenu(orderMenuList, storeId);

    MemberInfoDto memberInfo =
        MemberInfoDto.builder()
            .email(member.getEmail())
            .name(member.getName())
            .phone(member.getPhone())
            .address(member.getAddress())
            .build();

    return OrderReceiptDto.builder()
        .orderId(order.getId())
        .orderStatus(order.getOrderStatus().toString())
        .memberInfo(memberInfo)
        .storeInfo(storeInfo)
        .totalPrice(order.getTotalPrice())
        .cartList(cartList)
        .build();
  }

  public List<OrderStoreDetailDTO> getStoreOrderInfoByStoreId(Long storeId) {
    List<OrderStoreDetailDTO> orderStoreDetailDTOs = new ArrayList<>();
    List<Order> orderList = orderRepository.findByStoreId(storeId);

    for (Order order : orderList) {

      List<OrderMenu> orderMenuList = orderMenuRepository.findByOrderId(order.getId());
      List<CartItemDTO> cartList = cartService.makeCartListByOrderMenu(orderMenuList, storeId);

      OrderStoreDetailDTO dto =
          OrderStoreDetailDTO.builder()
              .orderId(order.getId())
              .orderCreatedAt(order.getCreatedAt())
              .orderStatus(order.getOrderStatus().toString())
              .totalPrice(order.getTotalPrice())
              .memberInfo(
                  MemberInfoDto.builder()
                      .email(order.getMember().getEmail())
                      .name(order.getMember().getName())
                      .phone(order.getMember().getPhone())
                      .address(order.getMember().getAddress())
                      .build())
              .cartList(cartList)
              .build();

      orderStoreDetailDTOs.add(dto);
    }

    return orderStoreDetailDTOs;
  }

  public void approveOrder(Long storeId, Long orderId) {
    Order order =
        orderRepository
            .findByStoreIdAndId(storeId, orderId)
            .orElseThrow(OrderNotFoundException::new);

    order.updateStatus(OrderStatus.APPROVED_ORDER);

    orderRepository.save(order);
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

  private OrderReceiptDto getOrderReceipt(
      Order order, List<CartItemDTO> cartList, long totalPrice, Long storeId, Member member) {

    StoreInfoDTO storeInfo = storeService.getStoreInfo(storeId);

    MemberInfoDto memberInfo =
        MemberInfoDto.builder()
            .email(member.getEmail())
            .name(member.getName())
            .phone(member.getPhone())
            .address(member.getAddress())
            .build();

    return OrderReceiptDto.builder()
        .orderId(order.getId())
        .orderStatus(order.getOrderStatus().toString())
        .memberInfo(memberInfo)
        .storeInfo(storeInfo)
        .totalPrice(totalPrice)
        .cartList(cartList)
        .build();
  }
}
