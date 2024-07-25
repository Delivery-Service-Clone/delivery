package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.StoreDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
  private final StoreRepository storeRepository;
  private final OwnerRepository ownerRepository;
  public void registerStore(StoreDto.request request) {

    Owner owner = ownerRepository.findById(request.getOwnerId()).orElseThrow();

    Store store = Store.builder()
        .owner(owner)
        .name(request.getStoreName())
        .address(request.getStoreAddress())
        .phone(request.getStorePhone())
        .openStatus(request.getOpenStatus())
        .introduction(request.getIntroduction())
        .build();

    storeRepository.save(store);
  }
}
