package com.example.delivery.domain.rider.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.delivery.TestContainerConfig;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.dto.OrderReceiptDto;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.rider.dto.DeliveryRiderDTO;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.repository.RiderRepository;
import com.example.delivery.domain.store.dto.StoreInfoDTO;
import com.example.delivery.domain.user.dto.MemberInfoDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@ExtendWith(TestContainerConfig.class)
public class DeliveryDaoTest {

  @Autowired
  @Qualifier("deliveryRedisTemplate")
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private RiderRepository riderRepository;

  private DeliveryDao deliveryDao;
  private DeliveryRiderDTO riderDto;
  private Rider rider;

  @BeforeEach
  public void setUp() {
    deliveryDao = new DeliveryDao(redisTemplate);

    // 공통적으로 사용할 라이더 정보 설정
    riderDto = new DeliveryRiderDTO("fcmToken123", "고등동");

    // 실제 Rider 엔티티 생성
    rider =
        Rider.builder()
            .email("example11@example.com")
            .name("John Doe")
            .password("password")
            .phone("010-1234-5678")
            .address("고등동")
            .build();
  }

  @Test
  @DisplayName("라이더 출근 시 레디스에 정상 등록되는지 테스트")
  public void testRegisterRiderWhenStartWork() {
    // Given
    String riderKey = "STANDBY_RIDERS:고등동";

    // When
    deliveryDao.registerRiderWhenStartWork(riderDto, rider);

    // Then
    Object storedToken = redisTemplate.opsForHash().get(riderKey, String.valueOf(rider.getId()));
    assertThat(storedToken).isEqualTo("fcmToken123");
  }

  @Test
  @DisplayName("라이더 배달 중으로 상태 변경 시 레디스에서 정상 삭제되는지 테스트")
  void testUpdateRiderStatusToDelivering() {
    // Given
    deliveryDao.registerRiderWhenStartWork(riderDto, rider);

    // When
    deliveryDao.deleteRider(riderDto, rider);

    // Then
    Object storedToken =
        redisTemplate.opsForHash().get("STANDBY_RIDERS:고등동", String.valueOf(rider.getId()));
    assertThat(storedToken).isNull();
  }

  @Test
  @DisplayName("주문이 레디스에 정상 등록되는지 테스트")
  public void testInsertStandbyOrder() {
    // Given
    MemberInfoDto memberInfo =
        new MemberInfoDto("user@example.com", "John Doe", "010-1234-5678", "고등동");
    StoreInfoDTO storeInfoDTO =
        new StoreInfoDTO(1L, "Burger King", "456 Elm St", "123-456-7890");

    List<CartItemDTO> cartItemDTOList = new ArrayList<>();
    CartItemDTO cartItemDTO = new CartItemDTO("New Menu", 15000L, 1L, 1L, 1L);
    cartItemDTOList.add(cartItemDTO);

    OrderReceiptDto orderReceipt =
        new OrderReceiptDto(1L, OrderStatus.APPROVED_ORDER.toString(), 15000L, memberInfo,
            storeInfoDTO, cartItemDTOList);
    String orderKey = "STANDBY_ORDERS:고등동";

    // When
    deliveryDao.insertStandbyOrder(1L, orderReceipt);

    // Then
    Object storedOrder = redisTemplate.opsForHash().get(orderKey, "1");
    assertThat(storedOrder).isNotNull();
    assertThat(((OrderReceiptDto) storedOrder).getOrderId()).isEqualTo(1L);
    assertThat(((OrderReceiptDto) storedOrder).getOrderStatus())
        .isEqualTo(OrderStatus.APPROVED_ORDER.toString());
    assertThat(((OrderReceiptDto) storedOrder).getTotalPrice()).isEqualTo(15000L);
    assertThat(((OrderReceiptDto) storedOrder).getMemberInfo().getAddress()).isEqualTo("고등동");
    assertThat(((OrderReceiptDto) storedOrder).getMemberInfo().getEmail())
        .isEqualTo("user@example.com");
    assertThat(((OrderReceiptDto) storedOrder).getMemberInfo().getName()).isEqualTo("John Doe");
    assertThat(((OrderReceiptDto) storedOrder).getMemberInfo().getPhone()).isEqualTo(
        "010-1234-5678");
    // 추가된 검증 코드
    assertThat(((OrderReceiptDto) storedOrder).getStoreInfo().getStoreId()).isEqualTo(1L);
    assertThat(((OrderReceiptDto) storedOrder).getStoreInfo().getStoreName()).isEqualTo(
        "Burger King");
    assertThat(((OrderReceiptDto) storedOrder).getStoreInfo().getStoreAddress()).isEqualTo(
        "456 Elm St");
    assertThat(((OrderReceiptDto) storedOrder).getStoreInfo().getStorePhone()).isEqualTo(
        "123-456-7890");

    List<CartItemDTO> storedCartItems = ((OrderReceiptDto) storedOrder).getCartList();
    assertThat(storedCartItems).isNotNull(); // 리스트가 null이 아닌지 확인
    assertThat(storedCartItems.size()).isEqualTo(1); // 리스트의 크기가 1인지 확인

    CartItemDTO storedCartItem = storedCartItems.get(0);
    assertThat(storedCartItem.getName()).isEqualTo("New Menu");
    assertThat(storedCartItem.getPrice()).isEqualTo(15000L);
    assertThat(storedCartItem.getMenuId()).isEqualTo(1L);
    assertThat(storedCartItem.getStoreId()).isEqualTo(1L);
    assertThat(storedCartItem.getCount()).isEqualTo(1L);
  }

  @Test
  @DisplayName("같은 지역의 라이더들에게 메시지를 보내기 위한 토큰 조회 테스트")
  void testGetRiderTokensByAddress() {
    // Given
    DeliveryRiderDTO riderDto1 = new DeliveryRiderDTO("fcmToken123", "고등동");
    DeliveryRiderDTO riderDto2 = new DeliveryRiderDTO("fcmToken456", "고등동");

    Rider riderEntity1 =
        Rider.builder()
            .email("example1@example.com")
            .name("John Doe")
            .password("password")
            .phone("010-1234-5678")
            .address("고등동")
            .build();

    Rider riderEntity2 =
        Rider.builder()
            .email("example2@example.com")
            .name("Jane Smith")
            .password("password")
            .phone("010-8765-4321")
            .address("고등동")
            .build();
    // 영속화하여 ID 생성
    riderEntity1 = riderRepository.save(riderEntity1);
    riderEntity2 = riderRepository.save(riderEntity2);

    // When
    deliveryDao.registerRiderWhenStartWork(riderDto1, riderEntity1);
    deliveryDao.registerRiderWhenStartWork(riderDto2, riderEntity2);

    // When
    Set<String> tokens = deliveryDao.getRiderTokensByAddress("고등동");
    System.out.println(tokens);

    // Then
    assertThat(tokens).containsExactlyInAnyOrder("fcmToken123", "fcmToken456");
  }

  @Test
  @DisplayName("라이더가 배달 가능한 지역의 주문 리스트 조회 테스트")
  void testSelectOrderList() {
    // Given
    MemberInfoDto userInfo1 =
        new MemberInfoDto("user1@example.com", "User One", "010-1234-5678", "고등동");
    StoreInfoDTO storeInfo1 =
        new StoreInfoDTO(1L, "Burger King", "456 Elm St", "123-456-7890");

    List<CartItemDTO> cartItemDTOList1 = new ArrayList<>();
    CartItemDTO cartItemDTO1 = new CartItemDTO("New Menu", 15000L, 1L, 1L, 1L);
    cartItemDTOList1.add(cartItemDTO1);

    OrderReceiptDto orderReceipt1 =
        new OrderReceiptDto(1L, OrderStatus.APPROVED_ORDER.toString(), 15000L, userInfo1,
            storeInfo1, cartItemDTOList1);

    MemberInfoDto userInfo2 =
        new MemberInfoDto("user2@example.com", "User Two", "010-8765-4321", "고등동");
    StoreInfoDTO storeInfo2 =
        new StoreInfoDTO(1L, "Burger King", "456 Elm St", "123-456-7890");

    List<CartItemDTO> cartItemDTOList2 = new ArrayList<>();
    CartItemDTO cartItemDTO2 = new CartItemDTO("New Menu2", 10000L, 2L, 1L, 2L);
    cartItemDTOList2.add(cartItemDTO2);

    OrderReceiptDto orderReceipt2 =
        new OrderReceiptDto(2L, OrderStatus.APPROVED_ORDER.toString(), 20000L, userInfo2,
            storeInfo2, cartItemDTOList2);

    deliveryDao.insertStandbyOrder(1L, orderReceipt1);
    deliveryDao.insertStandbyOrder(2L, orderReceipt2);

    // When
    List<String> orderList = deliveryDao.selectOrderList("고등동");

    // Then
    assertThat(orderList).containsExactlyInAnyOrder("1", "2");
  }

  @Test
  @DisplayName("라이더가 배달을 수락 할 때 주문 정보와 라이더 정보 삭제 테스트 (라이더가 존재할 때)")
  void testUpdateOrderToDelivering_whenRiderExists() {
    // Given
    MemberInfoDto userInfo =
        new MemberInfoDto("user1@example.com", "User One", "010-1234-5678", "고등동");
    StoreInfoDTO storeInfo =
        new StoreInfoDTO(1L, "Burger King", "456 Elm St", "123-456-7890");

    List<CartItemDTO> cartItemDTOList = new ArrayList<>();
    CartItemDTO cartItemDTO = new CartItemDTO("New Menu2", 10000L, 2L, 1L, 1L);
    cartItemDTOList.add(cartItemDTO);

    OrderReceiptDto orderReceipt =
        new OrderReceiptDto(1L, OrderStatus.APPROVED_ORDER.toString(), 10000L, userInfo, storeInfo,
            cartItemDTOList);

    deliveryDao.registerRiderWhenStartWork(riderDto, rider);
    deliveryDao.insertStandbyOrder(1L, orderReceipt);

    // When
    deliveryDao.updateOrderToDelivering(1L, riderDto, rider);

    // Then
    Object storedRiderToken =
        redisTemplate.opsForHash().get("STANDBY_RIDERS:고등동", String.valueOf(rider.getId()));
    Object storedOrder = redisTemplate.opsForHash().get("STANDBY_ORDERS:고등동", "1");

    assertThat(storedRiderToken).isNull();
    assertThat(storedOrder).isNull();
  }

  @Test
  @DisplayName("라이더가 배달을 수락 할 때 주문 정보만 삭제 테스트 (라이더가 존재하지 않을 때)")
  void testUpdateOrderToDelivering_whenRiderDoesNotExist() {
    // Given
    MemberInfoDto userInfo =
        new MemberInfoDto("user1@example.com", "User One", "010-1234-5678", "고등동");
    StoreInfoDTO storeInfo =
        new StoreInfoDTO(1L, "Burger King", "456 Elm St", "123-456-7890");

    List<CartItemDTO> cartItemDTOList = new ArrayList<>();
    CartItemDTO cartItemDTO = new CartItemDTO("New Menu2", 10000L, 2L, 1L, 1L);
    cartItemDTOList.add(cartItemDTO);

    OrderReceiptDto orderReceipt =
        new OrderReceiptDto(1L, OrderStatus.APPROVED_ORDER.toString(), 10000L, userInfo, storeInfo,
            cartItemDTOList);

    deliveryDao.insertStandbyOrder(1L, orderReceipt);

    // When
    deliveryDao.updateOrderToDelivering(1L, riderDto, rider);

    // Then
    Object storedRiderToken =
        redisTemplate.opsForHash().get("STANDBY_RIDERS:고등동", String.valueOf(rider.getId()));
    Object storedOrder = redisTemplate.opsForHash().get("STANDBY_ORDERS:고등동", "1");

    assertThat(storedRiderToken).isNull();
    assertThat(storedOrder).isNull();
  }
}
