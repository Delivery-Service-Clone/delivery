package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findByCategory(Category category);
  Slice<Store> findByAddressAndCategoryAndIdGreaterThan(String address, Category category, Long id, Pageable pageable);
}
