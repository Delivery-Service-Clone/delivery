package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.StoreDto;
import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private OwnerRepository ownerRepository;

  @InjectMocks
  private StoreService storeService;

  private Owner owner;
  private Store store;

  @BeforeEach
  void setUp() {
    owner = Owner.builder()
        .email("owner@example.com")
        .name("John Doe")
        .password("securepassword")
        .phone("123-456-7890")
        .address("123 Main St")
        .build();

    store = Store.builder()
        .owner(owner)
        .category(Category.BURGER)
        .name("Burger King")
        .phone("123-456-7890")
        .address("456 Elm St")
        .storeStatus(StoreStatus.STORE_OPEN)
        .introduction("Best burgers")
        .build();
  }

  @Test
  @DisplayName("Store를 성공적으로 등록한다")
  void registerStore() {
    // Given
    StoreCreateDto storeCreateDto = new StoreCreateDto(
        "Burger King",
        "123-456-7890",
        "456 Elm St",
        StoreStatus.STORE_OPEN,
        "Best burgers",
        Category.BURGER);

    // When
    storeService.registerStore(storeCreateDto, owner);

    // Then
    ArgumentCaptor<Store> storeArgumentCaptor = ArgumentCaptor.forClass(Store.class);
    verify(storeRepository).save(storeArgumentCaptor.capture());
    Store savedStore = storeArgumentCaptor.getValue();
    assertThat(savedStore.getName()).isEqualTo("Burger King");
    assertThat(savedStore.getOwner()).isEqualTo(owner);
  }

  @Test
  @DisplayName("Store의 상태를 성공적으로 변경한다")
  void changeStoreStatus() {
    // Given
    store.updateStatus(StoreStatus.STORE_OPEN);
    when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

    // When
    storeService.changeStoreStatus(1L, StoreStatus.STORE_CLOSE);

    // Then
    verify(storeRepository).save(store);
    assertThat(store.getStoreStatus()).isEqualTo(StoreStatus.STORE_CLOSE);
  }

  @Test
  @DisplayName("Store가 존재하지 않으면 상태 변경 시도 시 예외가 발생한다")
  void changeStoreStatus_storeNotFound() {
    // Given
    when(storeRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(StoreNotFoundException.class, () -> storeService.changeStoreStatus(1L, StoreStatus.STORE_CLOSE));
  }

  @Test
  @DisplayName("모든 Store를 성공적으로 조회한다")
  void getAllStores() {
    // Given
    Store store2 = Store.builder()
        .owner(owner)
        .category(Category.KOREAN)
        .name("Korean BBQ")
        .phone("111-111-1111")
        .address("123 Main St")
        .storeStatus(StoreStatus.STORE_OPEN)
        .introduction("Delicious Korean BBQ")
        .build();

    when(storeRepository.findAll()).thenReturn(Arrays.asList(store, store2));

    // When
    List<StoreDto> stores = storeService.getAllStores();

    // Then
    assertThat(stores).hasSize(2);
    assertThat(stores.get(0).getStoreName()).isEqualTo("Burger King");
    assertThat(stores.get(1).getStoreName()).isEqualTo("Korean BBQ");
  }

  @Test
  @DisplayName("Store가 하나도 없을 시 조회 시도 시 예외가 발생한다")
  void getAllStores_storeNotFound() {
    // Given
    when(storeRepository.findAll()).thenReturn(List.of());

    // When & Then
    assertThrows(StoreNotFoundException.class, () -> storeService.getAllStores());
  }

  @Test
  @DisplayName("카테고리로 Store를 성공적으로 조회한다")
  void getStoresByCategory() {
    // Given
    when(storeRepository.findByCategory(Category.BURGER)).thenReturn(List.of(store));

    // When
    List<StoreDto> stores = storeService.getStoresByCategory(Category.BURGER);

    // Then
    assertThat(stores).hasSize(1);
    assertThat(stores.get(0).getStoreName()).isEqualTo("Burger King");
  }

  @Test
  @DisplayName("해당 카테고리에 Store가 없을 경우 조회 시도 시 예외가 발생한다")
  void getStoresByCategory_storeNotFound() {
    // Given
    when(storeRepository.findByCategory(Category.BURGER)).thenReturn(List.of());

    // When & Then
    assertThrows(StoreNotFoundException.class, () -> storeService.getStoresByCategory(Category.BURGER));
  }
}