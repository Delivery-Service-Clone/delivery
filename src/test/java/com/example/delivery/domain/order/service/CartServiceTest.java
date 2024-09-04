package com.example.delivery.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.domain.menu.exception.MenuNotFoundException;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.exception.CartNotFoundException;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

  // 테스트 주체의 외부 의존성들 -> Mock 객체로 생성하기
  @Mock
  private CartItemDAO cartItemDAO;
  @Mock
  private StoreRepository storeRepository;
  @Mock
  private MenuRepository menuRepository;
  // 단위 테스트의 주체 -> @InjectMocks로 가짜 의존성 주입받기
  @InjectMocks
  private CartService cartService;

  private Member member;
  private CartItemDTO cartItemDTO;

  @BeforeEach
  void setUp() {
    // given
    member =
        Member.builder()
            .email("example@example.com")
            .name("테스트")
            .password("password")
            .phone("010-1234-5678")
            .address("Paris")
            .build();

    cartItemDTO = new CartItemDTO("new Item", 7000L, 1L, 1L, 2L);
  }

  @Test
  @DisplayName("장바구니 등록이 잘 되었는 지 테스트")
  void testRegisterMenuInCart_Success() {
    // given
    when(storeRepository.existsById(cartItemDTO.getStoreId())).thenReturn(true);
    when(menuRepository.existsById(cartItemDTO.getMenuId())).thenReturn(true);

    // when 이 메서드는 주어진 실행 블록이 예외 없이 성공적으로 완료될 것을 기대
    assertDoesNotThrow(() -> cartService.registerMenuInCart(member.getEmail(), cartItemDTO));

    // then
    verify(cartItemDAO).insertMenu(member.getEmail(), cartItemDTO);
  }

  @Test
  @DisplayName("장바구니에 넣을 내역이 존재하지 않을 때 오류를 내보내는 지 테스트")
  void testRegisterMenuInCart_NullCart() {
    // given
    CartItemDTO nullCartItem = null;

    // when + then
    assertThrows(CartNotFoundException.class, () -> {
      cartService.registerMenuInCart(member.getEmail(), nullCartItem);
    });
  }

  @Test
  @DisplayName("장바구니에 넣을 내역 중 Store Id 값에 해당하는 Store가 존재하지 않을 때 오류를 내보내는 지 테스트")
  void testRegisterMenuInCart_StoreNotFound() {
    // given
    when(storeRepository.existsById(cartItemDTO.getStoreId())).thenReturn(false);

    // when + Then
    assertThrows(StoreNotFoundException.class, () -> {
      cartService.registerMenuInCart(member.getEmail(), cartItemDTO);
    });

  }

  @Test
  @DisplayName("장바구니에 넣을 내역 중 Menu Id 값에 해당하는 Menu가 존재하지 않을 때 오류를 내보내는 지 테스트")
  void testRegisterMenuInCart_MenuNotFound() {
    // given
    when(storeRepository.existsById(cartItemDTO.getStoreId())).thenReturn(true);
    when(menuRepository.existsById(cartItemDTO.getMenuId())).thenReturn(false);

    // when + then
    assertThrows(MenuNotFoundException.class, () -> {
      cartService.registerMenuInCart(member.getEmail(), cartItemDTO);
    });
  }
}
