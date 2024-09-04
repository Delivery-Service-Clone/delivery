package com.example.delivery.domain.order.dao;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.delivery.TestContainerConfig;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.user.entity.Member;
import java.util.ArrayList;
import java.util.List;
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
public class CartItemDAOTest {

  @Autowired
  @Qualifier("cartItemDTORedisTemplate")
  private RedisTemplate<String, CartItemDTO> redisTemplate;

  private CartItemDAO cartItemDAO;
  private Member member;

  @BeforeEach
  public void setUp() {
    cartItemDAO = new CartItemDAO(redisTemplate);

    // 고정된 사용자 엔티티 생성
    member =
        Member.builder()
            .email("test1@test.com")
            .name("Burger")
            .password("password")
            .phone("010-1234-5678")
            .address("San Francisco, CA")
            .build();
  }

  @Test
  @DisplayName("하나의 메뉴가 Redis에 정상 등록되는 지 테스트")
  public void testInsertMenu() {
    // Given
    String userId = member.getEmail();
    String cartKey = "test1@test.com:CART";
    CartItemDTO newItem = new CartItemDTO("New Item", 5000L, 1L, 1L, 2L);

    // When
    cartItemDAO.insertMenu(userId, newItem);

    // Then
    CartItemDTO storedCart = redisTemplate.opsForList().leftPop(cartKey);
    assertThat(storedCart).isNotNull();
    assertThat(storedCart.getName()).isEqualTo("New Item");
    assertThat(storedCart.getPrice()).isEqualTo(5000L);
    assertThat(storedCart.getMenuId()).isEqualTo(1L);
    assertThat(storedCart.getStoreId()).isEqualTo(1L);
    assertThat(storedCart.getCount()).isEqualTo(2L);
  }

  @Test
  @DisplayName("장바구니 리스트가 잘 출력되는 지 테스트")
  public void testSelectCartList() {
    //Given
    String userId = member.getEmail();
    String cartKey = "test1@test.com:CART";
    CartItemDTO newItem1 = new CartItemDTO("New Item1", 5000L, 1L, 1L, 2L);
    CartItemDTO newItem2 = new CartItemDTO("New Item2", 7000L, 2L, 1L, 1L);

    //When
    cartItemDAO.insertMenu(userId, newItem1);
    cartItemDAO.insertMenu(userId, newItem2);

    //Then
    List<CartItemDTO> cartList = cartItemDAO.selectCartList(userId);

    assertThat(cartList.size()).isEqualTo(2);
    assertThat(cartList.get(0).getName()).isEqualTo("New Item1");
    assertThat(cartList.get(0).getPrice()).isEqualTo(5000L);
    assertThat(cartList.get(0).getMenuId()).isEqualTo(1L);
    assertThat(cartList.get(0).getStoreId()).isEqualTo(1L);
    assertThat(cartList.get(0).getCount()).isEqualTo(2L);

    assertThat(cartList.get(1).getName()).isEqualTo("New Item2");
    assertThat(cartList.get(1).getPrice()).isEqualTo(7000L);
    assertThat(cartList.get(1).getMenuId()).isEqualTo(2L);
    assertThat(cartList.get(1).getStoreId()).isEqualTo(1L);
    assertThat(cartList.get(1).getCount()).isEqualTo(1L);
  }

  @Test
  @DisplayName("장바구니 Redis Pipeline을 통해 여러 개의 메뉴가 한번에 등록되는 지 테스트")
  public void testInsertMenuList() {
    //Given
    String userId = member.getEmail();
    String cartKey = "test1@test.com:CART";

    List<CartItemDTO> cartList = new ArrayList<>();
    CartItemDTO newItem1 = new CartItemDTO("New Item1", 5000L, 1L, 1L, 2L);
    CartItemDTO newItem2 = new CartItemDTO("New Item2", 7000L, 2L, 1L, 1L);
    CartItemDTO newItem3 = new CartItemDTO("New Item3", 6000L, 3L, 1L, 3L);
    CartItemDTO newItem4 = new CartItemDTO("New Item4", 9000L, 4L, 1L, 1L);
    CartItemDTO newItem5 = new CartItemDTO("New Item5", 11000L, 5L, 1L, 1L);

    cartList.add(newItem1);
    cartList.add(newItem2);
    cartList.add(newItem3);
    cartList.add(newItem4);
    cartList.add(newItem5);

    //When
    cartItemDAO.insertMenuList(userId, cartList);

    //Then
    List<CartItemDTO> storedCartList = cartItemDAO.selectCartList(userId);
    assertThat(storedCartList.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("장바구니 목록을 반환하고 나서 Redis에 저장한 Key 값이 사라지는 지 테스트")
  public void testGetCartAndDelete() {
    //Given
    String userId = member.getEmail();
    String cartKey = "test1@test.com:CART";

    List<CartItemDTO> cartList = new ArrayList<>();
    CartItemDTO newItem1 = new CartItemDTO("New Item1", 5000L, 1L, 1L, 2L);
    CartItemDTO newItem2 = new CartItemDTO("New Item2", 7000L, 2L, 1L, 1L);

    cartList.add(newItem1);
    cartList.add(newItem2);

    //When
    cartItemDAO.insertMenuList(userId, cartList);
    List<CartItemDTO> storedCartList = cartItemDAO.getCartAndDelete(userId);

    //Then
    assertThat(storedCartList.size()).isEqualTo(2);

    //Then
    assertThat(redisTemplate.opsForList().leftPop(cartKey)).isNull();
  }

  @Test
  @DisplayName("장바구니 제거를 했을 때 잘 제거되는 지 테스트")
  public void testDeleteMenuList() {
    //Given
    String userId = member.getEmail();
    String cartKey = "test1@test.com:CART";
    CartItemDTO newItem = new CartItemDTO("New Item", 5000L, 1L, 1L, 2L);

    //When
    cartItemDAO.insertMenu(userId, newItem);

    //When
    cartItemDAO.deleteMenuList(userId);

    //Then
    assertThat(redisTemplate.opsForList().leftPop(cartKey)).isNull();
  }
}
