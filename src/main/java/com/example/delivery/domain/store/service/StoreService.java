package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.exception.OwnerNotFoundException;
import com.example.delivery.domain.user.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private final OwnerRepository ownerRepository;

  public void registerStore(StoreCreateDto storeCreateDto) {

    Owner owner =
        ownerRepository
            .findById(storeCreateDto.getOwnerId())
            .orElseThrow(OwnerNotFoundException::new);

    Store store = storeCreateDto.toEntity(storeCreateDto, owner);

    storeRepository.save(store);
  }

  public void changeStoreStatus(Long storeId, StoreStatus status) {

    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
    store.updateStatus(status);
    storeRepository.save(store);
  }
}
