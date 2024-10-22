package com.example.delivery.domain.order.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.exception.MenuNotFoundException;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dao.CartItemDAO;
import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.exception.CartNotFoundException;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

  @Mock private CartItemDAO cartItemDAO;
  @Mock private StoreRepository storeRepository;
  @Mock private MenuRepository menuRepository;

  @InjectMocks private CartService cartService;

  private Member member;
  private CartItemDTO cartItemDTO;

  @BeforeEach
  void setUp() {
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
    when(storeRepository.existsById(cartItemDTO.getStoreId())).thenReturn(true);
    when(menuRepository.existsById(cartItemDTO.getMenuId())).thenReturn(true);

    assertDoesNotThrow(() -> cartService.registerMenuInCart(member.getEmail(), cartItemDTO));

    verify(cartItemDAO).insertMenu(member.getEmail(), cartItemDTO);
  }

  @Test
  @DisplayName("장바구니에 넣을 내역이 존재하지 않을 때 오류를 내보내는 지 테스트")
  void testRegisterMenuInCart_NullCart() {
    CartItemDTO nullCartItem = null;

    assertThrows(
        CartNotFoundException.class,
        () -> {
          cartService.registerMenuInCart(member.getEmail(), nullCartItem);
        });
  }

  @Test
  @DisplayName("장바구니에 넣을 내역 중 Store Id 값에 해당하는 Store가 존재하지 않을 때 오류를 내보내는 지 테스트")
  void testRegisterMenuInCart_StoreNotFound() {
    when(storeRepository.existsById(cartItemDTO.getStoreId())).thenReturn(false);

    assertThrows(
        StoreNotFoundException.class,
        () -> {
          cartService.registerMenuInCart(member.getEmail(), cartItemDTO);
        });
  }

  @Test
  @DisplayName("장바구니에 넣을 내역 중 Menu Id 값에 해당하는 Menu가 존재하지 않을 때 오류를 내보내는 지 테스트")
  void testRegisterMenuInCart_MenuNotFound() {
    when(storeRepository.existsById(cartItemDTO.getStoreId())).thenReturn(true);
    when(menuRepository.existsById(cartItemDTO.getMenuId())).thenReturn(false);

    assertThrows(
        MenuNotFoundException.class,
        () -> {
          cartService.registerMenuInCart(member.getEmail(), cartItemDTO);
        });
  }

  @Test
  @DisplayName("장바구니 불러오기 테스트")
  void testLoadCart_Success() {
    when(cartItemDAO.selectCartList(member.getEmail())).thenReturn(List.of(cartItemDTO));

    assertDoesNotThrow(() -> cartService.loadCart(member.getEmail()));
    verify(cartItemDAO).selectCartList(member.getEmail());
  }

  @Test
  @DisplayName("장바구니 전체 삭제 테스트")
  void testDeleteAllMenuInCart_Success() {
    assertDoesNotThrow(() -> cartService.deleteAllMenuInCart(member.getEmail()));

    verify(cartItemDAO).deleteMenuList(member.getEmail());
  }

  @Test
  @DisplayName("주문 메뉴로 장바구니 항목 생성 테스트")
  void testMakeCartListByOrderMenu_Success() {
    Menu menu = Menu.builder().id(1L).name("테스트 메뉴").price(10000L).build();

    OrderMenu orderMenu = OrderMenu.builder().menu(menu).count(2L).build();

    List<OrderMenu> orderMenuList = List.of(orderMenu);
    Long storeId = 1L;

    List<CartItemDTO> result = cartService.makeCartListByOrderMenu(orderMenuList, storeId);

    assertEquals(1, result.size());
    assertEquals("테스트 메뉴", result.get(0).getName());
    assertEquals(10000L, result.get(0).getPrice());
    assertEquals(2, result.get(0).getCount());
    assertEquals(storeId, result.get(0).getStoreId());
  }
}
