package com.example.delivery.domain.rider.service;

import static org.mockito.Mockito.*;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.exception.OrderNotFoundException;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {

  @Mock
  private DeliveryDao deliveryDao;

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private RiderService riderService;

  private Rider rider;
  private DeliveryRiderDTO riderDto;
  private Order order;

  @BeforeEach
  void setUp() {
    // given
    rider = Rider.builder()
        .email("example@example.com")
        .name("John Doe")
        .password("password")
        .phone("010-1234-5678")
        .address("고등동")
        .build();

    riderDto = new DeliveryRiderDTO("fcmToken123", "고등동");


    order = Order.builder()
        .orderStatus(OrderStatus.APPROVED_ORDER)
        .build();
    ReflectionTestUtils.setField(order, "id", 1L);

  }

  @Test
  @DisplayName("라이더가 출근할 때 레디스에 정상 등록된다")
  void testRegisterStandbyRiderWhenStartWork() {

    // when: 라이더가 출근을 등록할 때
    riderService.registerStandbyRiderWhenStartWork(riderDto, rider);

    // then: 레디스에 정상적으로 라이더 정보가 등록되어야 한다.
    verify(deliveryDao, times(1)).registerRiderWhenStartWork(riderDto, rider);
  }

  @Test
  @DisplayName("라이더가 퇴근할 때 레디스에서 정상 삭제된다")
  void testDeleteStandbyRiderWhenStopWork() {

    // when: 라이더가 퇴근을 등록할 때
    riderService.deleteStandbyRiderWhenStopWork(riderDto, rider);

    // then: 레디스에서 정상적으로 라이더 정보가 삭제되어야 한다.
    verify(deliveryDao, times(1)).deleteRider(riderDto, rider);
  }

  @Test
  @DisplayName("라이더가 배달 대기 주문을 수락할 때 주문 상태가 DELIVERING으로 변경된다")
  void testAcceptStandbyOrder() {
    // given: 주문이 존재하고 라이더가 주문을 수락할 준비가 되어있다.
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    // when: 라이더가 배달 대기 주문을 수락할 때
    riderService.acceptStandbyOrder(1L, riderDto, rider);

    // then: 주문 상태는 DELIVERING으로 변경되어야 하고, 데이터베이스와 레디스에 반영된다.
    verify(orderRepository, times(1)).save(order);
    verify(deliveryDao, times(1)).updateOrderToDelivering(1L, riderDto, rider);
    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.DELIVERING);
  }

  @Test
  @DisplayName("존재하지 않는 주문을 수락할 때 예외가 발생한다")
  void testAcceptStandbyOrderThrowsOrderNotFoundException() {
    // given: 주문이 존재하지 않는다.
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    // when, then: 라이더가 주문을 수락하려 할 때 OrderNotFoundException 예외가 발생해야 한다.
    assertThrows(OrderNotFoundException.class, () -> {
      riderService.acceptStandbyOrder(1L, riderDto, rider);
    });
  }

  @Test
  @DisplayName("배달 완료 시 주문 상태가 COMPLETE_DELIVERY로 변경된다")
  void testFinishDeliveringOrder() {
    // given: 주문이 존재하고 배달이 완료될 준비가 되어있다.
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    // when: 라이더가 배달을 완료할 때
    riderService.finishDeliveringOrder(1L, riderDto, rider);

    // then: 주문 상태는 COMPLETE_DELIVERY로 변경되어야 하고, 데이터베이스와 레디스에 반영된다.
    verify(orderRepository, times(1)).save(order);
    verify(deliveryDao, times(1)).registerRiderWhenStartWork(riderDto, rider);
    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE_DELIVERY);
  }
}