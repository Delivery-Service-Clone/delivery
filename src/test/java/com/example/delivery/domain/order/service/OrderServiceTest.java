package com.example.delivery.domain.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.order.dto.OrderStoreDetailDTO;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.exception.OrderNotFoundException;
import com.example.delivery.domain.order.repository.OrderMenuRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.pay.entity.PayType;
import com.example.delivery.domain.pay.service.PayService;
import com.example.delivery.domain.pay.service.PayServiceFactory;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.domain.user.entity.Member;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @Mock private CartItemDAO cartItemDAO;
  @Mock private StoreService storeService;
  @Mock private MenuService menuService;
  @Mock private CartService cartService;
  @Mock private OrderRepository orderRepository;
  @Mock private OrderMenuRepository orderMenuRepository;
  @Mock private PayServiceFactory payServiceFactory;

  @InjectMocks private OrderService orderService;

  private Member member;
  private Store store;
  private Order order;

  @BeforeEach
  void setUp() {
    member = Member.builder().email("test@email.com").build();

    store = Store.builder().name("test store").build();

    order =
        Order.builder().member(member).store(store).orderStatus(OrderStatus.BEFORE_ORDER).build();
    ReflectionTestUtils.setField(order, "id", 1L);
  }

  @Test
  @DisplayName("registerOrder 메서드 트랜잭션 정상 실행 테스트")
  void registerOrder_success_withMockedTransaction() {
    // Given
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(cartItemDAO.getCartAndDelete(member.getEmail())).thenReturn(new ArrayList<>());
    when(storeService.getStoreByStoreId(anyLong())).thenReturn(store);
    when(payServiceFactory.getPayService(any(PayType.class))).thenReturn(mock(PayService.class));

    // Mock the TransactionSynchronizationManager to simulate active transaction
    try (MockedStatic<TransactionSynchronizationManager> mockedTransactionSync =
        mockStatic(TransactionSynchronizationManager.class)) {
      // 트랜잭션이 활성화된 것처럼 설정
      mockedTransactionSync
          .when(TransactionSynchronizationManager::isSynchronizationActive)
          .thenReturn(true);
      mockedTransactionSync
          .when(
              () ->
                  TransactionSynchronizationManager.registerSynchronization(
                      any(TransactionSynchronizationAdapter.class)))
          .thenAnswer(
              invocation -> {
                TransactionSynchronizationAdapter adapter = invocation.getArgument(0);
                adapter.afterCompletion(
                    TransactionSynchronizationAdapter.STATUS_COMMITTED); // 트랜잭션 커밋된 것처럼 동작
                return null;
              });

      // When
      OrderReceiptDto receipt = orderService.registerOrder(member, 1L, PayType.CARD);

      // Then
      assertNotNull(receipt);
      verify(orderRepository, times(1)).save(any(Order.class));
      verify(cartItemDAO, times(1)).getCartAndDelete(member.getEmail());
      verify(cartItemDAO, never())
          .insertMenuList(anyString(), anyList()); // 정상적인 경우 rollback이 발생하지 않음
    }
  }

  @Test
  @DisplayName("getStoreOrderInfoByStoreId 메서드 테스트")
  void getStoreOrderInfoByStoreId_success() {
    List<Order> orders = Collections.singletonList(order);
    when(orderRepository.findByStoreId(anyLong())).thenReturn(orders);
    doReturn(new ArrayList<>()).when(orderMenuRepository).findByOrderId(anyLong());

    List<OrderStoreDetailDTO> storeOrderInfo = orderService.getStoreOrderInfoByStoreId(1L);

    assertEquals(1, storeOrderInfo.size());
    verify(orderRepository, times(1)).findByStoreId(anyLong());
  }

  @Test
  @DisplayName("getOrderInfoByOrderId 메서드 테스트")
  void getOrderInfoByOrderId_success() {
    when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
    when(orderMenuRepository.findByOrderId(anyLong())).thenReturn(new ArrayList<>());

    OrderReceiptDto orderInfo = orderService.getOrderInfoByOrderId(member, 1L, 1L);

    assertNotNull(orderInfo);
    verify(orderRepository, times(1)).findById(anyLong());
  }

  @Test
  @DisplayName("approveOrder 메서드 정상 실행 테스트")
  void approveOrder_success() {
    when(orderRepository.findByStoreIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(order));

    orderService.approveOrder(1L, 1L);

    assertEquals(OrderStatus.APPROVED_ORDER, order.getOrderStatus());
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  @DisplayName("approveOrder 메서드 오류 발생 테스트")
  void approveOrder_orderNotFoundException() {
    when(orderRepository.findByStoreIdAndId(anyLong(), anyLong())).thenReturn(Optional.empty());

    assertThrows(OrderNotFoundException.class, () -> orderService.approveOrder(1L, 1L));
  }
}
