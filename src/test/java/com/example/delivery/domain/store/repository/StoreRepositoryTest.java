package com.example.delivery.domain.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
class StoreRepositoryTest {

  @Autowired private StoreRepository storeRepository;

  @Autowired private OwnerRepository ownerRepository;

  private Owner owner;

  @BeforeEach
  void setUp() {
    // 고유한 이메일을 가진 Owner를 미리 저장
    String uniqueEmail = "owner+" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    owner =
        Owner.builder()
            .email(uniqueEmail)
            .name("John Doe")
            .password("securepassword")
            .phone("123-456-7890")
            .address("123 Main St")
            .build();

    ownerRepository.save(owner);
  }

  @Test
  void testFindByCategory() {
    // Given
    Store store1 =
        Store.builder()
            .owner(owner)
            .category(Category.KOREAN)
            .name("Korean BBQ")
            .phone("111-111-1111")
            .address("Address 1")
            .storeStatus(StoreStatus.STORE_OPEN)
            .introduction("Delicious Korean BBQ")
            .build();

    Store store2 =
        Store.builder()
            .owner(owner)
            .category(Category.BURGER)
            .name("Burger King")
            .phone("222-222-2222")
            .address("Address 2")
            .storeStatus(StoreStatus.STORE_OPEN)
            .introduction("Fast and tasty")
            .build();

    storeRepository.save(store1);
    storeRepository.save(store2);

    // When
    List<Store> koreanStores = storeRepository.findByCategory(Category.KOREAN);

    // Then
    assertThat(koreanStores).hasSize(1);
    assertThat(koreanStores.get(0).getName()).isEqualTo("Korean BBQ");
  }
}