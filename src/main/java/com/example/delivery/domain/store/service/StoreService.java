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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final OwnerRepository ownerRepository;

  public void registerStore(StoreCreateDto storeCreateDto, Owner owner) {

    Store store = storeCreateDto.toEntity(storeCreateDto, owner);

    storeRepository.save(store);
  }

  public void changeStoreStatus(Long storeId, StoreStatus status) {

    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
    store.updateStatus(status);
    storeRepository.save(store);
  }

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
}
