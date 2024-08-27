package com.example.delivery.domain.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.example.delivery.domain.menu.dto.MenuDto;
import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.exception.MenuNotFoundException;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

  @Mock private OwnerRepository ownerRepository;

  @Mock private MenuRepository menuRepository;

  @Mock private StoreRepository storeRepository;

  @InjectMocks private MenuService menuService;

  private Store store;

  @BeforeEach
  void setUp() {
    Owner owner =
        Owner.builder()
            .email("owner@example.com")
            .name("John Doe")
            .password("securepassword")
            .phone("123-456-7890")
            .address("123 Main St")
            .build();

    store =
        Store.builder()
            .owner(owner)
            .category(Category.KOREAN)
            .name("Korean BBQ")
            .phone("111-111-1111")
            .address("Address 1")
            .storeStatus(StoreStatus.STORE_OPEN)
            .introduction("Delicious Korean BBQ")
            .build();
  }

  @Test
  @DisplayName("메뉴를 성공적으로 등록한다")
  void registerMenuSuccess() {
    // Given
    MenuCreateDto menuCreateDto =
        MenuCreateDto.builder()
            .storeId(1L)
            .menuName("New Menu")
            .price(12000L)
            .description("New Menu Description")
            .photo("photo.jpg")
            .build();

    given(storeRepository.findById(1L)).willReturn(Optional.of(store));

    // When
    menuService.registerMenu(menuCreateDto);

    // Then
    verify(menuRepository, times(1)).save(any(Menu.class));
  }

  @Test
  @DisplayName("메뉴 등록 시 가게를 찾을 수 없으면 예외를 던진다")
  void registerMenuThrowsExceptionWhenStoreNotFound() {
    // Given
    MenuCreateDto menuCreateDto =
        MenuCreateDto.builder()
            .storeId(1L)
            .menuName("New Menu")
            .price(12000L)
            .description("New Menu Description")
            .photo("photo.jpg")
            .build();

    given(storeRepository.findById(1L)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> menuService.registerMenu(menuCreateDto))
        .isInstanceOf(StoreNotFoundException.class);
  }

  @Test
  @DisplayName("특정 가게의 메뉴 리스트를 성공적으로 조회한다")
  void getMenusByStoreIdSuccess() {
    // Given
    Menu menu1 =
        Menu.builder()
            .store(store)
            .name("Menu 1")
            .price(10000L)
            .description("Delicious food")
            .photo("photo1.jpg")
            .build();

    Menu menu2 =
        Menu.builder()
            .store(store)
            .name("Menu 2")
            .price(15000L)
            .description("Even more delicious food")
            .photo("photo2.jpg")
            .build();

    given(storeRepository.findById(1L)).willReturn(Optional.of(store));
    given(menuRepository.findByStore(store)).willReturn(List.of(menu1, menu2));

    // When
    List<MenuDto> menus = menuService.getMenusByStoreId(1L);

    // Then
    assertThat(menus).hasSize(2);
    assertThat(menus).extracting(MenuDto::getName).containsExactlyInAnyOrder("Menu 1", "Menu 2");
  }

  @Test
  @DisplayName("특정 가게의 메뉴 리스트 조회 시 메뉴가 없으면 예외를 던진다")
  void getMenusByStoreIdThrowsExceptionWhenNoMenuFound() {
    // Given
    given(storeRepository.findById(1L)).willReturn(Optional.of(store));
    given(menuRepository.findByStore(store)).willReturn(List.of());

    // When & Then
    assertThatThrownBy(() -> menuService.getMenusByStoreId(1L))
        .isInstanceOf(MenuNotFoundException.class);
  }

  @Test
  @DisplayName("특정 가게의 메뉴 리스트 조회 시 가게를 찾을 수 없으면 예외를 던진다")
  void getMenusByStoreIdThrowsExceptionWhenStoreNotFound() {
    // Given
    given(storeRepository.findById(1L)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> menuService.getMenusByStoreId(1L))
        .isInstanceOf(StoreNotFoundException.class);
  }
}
