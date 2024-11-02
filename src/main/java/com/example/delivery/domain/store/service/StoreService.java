package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.StoreDto;
import com.example.delivery.domain.store.dto.StoreInfoDTO;
import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final OwnerRepository ownerRepository;

  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = "storesByCategory", key = "#storeCreateDto.category.name()"),
      })
  public void registerStore(StoreCreateDto storeCreateDto, Owner owner) {

    Store store = storeCreateDto.toEntity(storeCreateDto, owner);

    storeRepository.save(store);
  }

  public void changeStoreStatus(Long storeId, StoreStatus status) {

    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
    store.updateStatus(status);
    storeRepository.save(store);
  }

  @Transactional(readOnly = true)
  public List<StoreDto> getAllStores() {

    List<Store> stores = storeRepository.findAll();

    if (stores.isEmpty()) {
      throw new StoreNotFoundException();
    }

    List<StoreDto> storeDtos =
        stores.stream()
            .map(
                store ->
                    StoreDto.builder()
                        .ownerId(store.getOwner().getId())
                        .storeAddress(store.getAddress())
                        .storeName(store.getName())
                        .storePhone(store.getPhone())
                        .storeStatus(store.getStoreStatus())
                        .introduction(store.getIntroduction())
                        .category(store.getCategory())
                        .build())
            .collect(Collectors.toList());

    return storeDtos;
  }

  @Transactional(readOnly = true)
  public List<StoreDto> getStoresByCursor(
      String address, Category category, Long lastId, int limit) {

    // Pageable 객체 생성 (페이지 크기와 정렬 설정)
    Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "id"));

    // 커서 기반으로 조건에 맞는 데이터 조회
    Slice<Store> storesSlice =
        storeRepository.findByAddressAndCategoryAndIdGreaterThan(
            address, category, lastId, pageable);

    // 조회된 Store 리스트가 비어 있는 경우 예외 처리
    if (storesSlice.isEmpty()) {
      throw new StoreNotFoundException();
    }

    // Store 엔티티 리스트를 StoreDto 리스트로 변환
    List<StoreDto> storeDtos =
        storesSlice.stream()
            .map(
                store ->
                    StoreDto.builder()
                        .storeId(store.getId())
                        .ownerId(store.getOwner().getId())
                        .storeAddress(store.getAddress())
                        .storeName(store.getName())
                        .storePhone(store.getPhone())
                        .storeStatus(store.getStoreStatus())
                        .introduction(store.getIntroduction())
                        .category(store.getCategory())
                        .build())
            .collect(Collectors.toList());
    return storeDtos;
  }

  @Transactional(readOnly = true)
  @Cacheable(value = "storesByCategory", key = "#category.name()")
  public List<StoreDto> getStoresByCategory(Category category) {
    List<Store> stores = storeRepository.findByCategory(category);

    if (stores.isEmpty()) {
      throw new StoreNotFoundException();
    }

    return stores.stream()
        .map(
            store ->
                StoreDto.builder()
                    .ownerId(store.getOwner().getId())
                    .storeAddress(store.getAddress())
                    .storeName(store.getName())
                    .storePhone(store.getPhone())
                    .storeStatus(store.getStoreStatus())
                    .introduction(store.getIntroduction())
                    .build())
        .collect(Collectors.toList());
  }

  public Store getStoreByStoreId(Long storeId) {
    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
    return store;
  }

  public StoreInfoDTO getStoreInfo(Long storeId) {
    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
    StoreInfoDTO storeInfoDTO =
        StoreInfoDTO.builder()
            .StoreId(storeId)
            .StoreAddress(store.getAddress())
            .StorePhone(store.getPhone())
            .StoreName(store.getName())
            .build();
    return storeInfoDTO;
  }
}
