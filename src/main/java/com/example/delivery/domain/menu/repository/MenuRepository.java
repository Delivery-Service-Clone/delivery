package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.store.entity.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

  List<Menu> findByStore(Store store);

  Optional<Menu> findMenuByStoreAndId(Store store, Long Id);
}
