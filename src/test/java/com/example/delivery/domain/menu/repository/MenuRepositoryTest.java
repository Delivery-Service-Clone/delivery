package com.example.delivery.domain.menu.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("MenuRepository 테스트")
class MenuRepositoryTest {

  @Autowired
  private MenuRepository menuRepository;
  @Autowired
  private OwnerRepository ownerRepository;
  @Autowired
  private StoreRepository storeRepository;

  @Test
  @DisplayName("특정 가게에 대한 메뉴를 조회할 수 있다")
  void findByStore() {
    // Given
    Owner owner =
        Owner.builder()
            .email("owner@example.com")
            .name("John Doe")
            .password("securepassword")
            .phone("123-456-7890")
            .address("123 Main St")
            .build();

    ownerRepository.save(owner);

    Store store =
        Store.builder()
            .owner(owner)
            .category(Category.KOREAN)
            .name("Korean BBQ")
            .phone("111-111-1111")
            .address("Address 1")
            .storeStatus(StoreStatus.STORE_OPEN)
            .introduction("Delicious Korean BBQ")
            .build();

    storeRepository.save(store);

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

    // When
    menuRepository.save(menu1);
    menuRepository.save(menu2);

    // Then
    List<Menu> menus = menuRepository.findByStore(store);
    assertThat(menus).hasSize(2);
    assertThat(menus).extracting(Menu::getName).containsExactlyInAnyOrder("Menu 1", "Menu 2");
  }
}
