package com.example.delivery.domain.menu.service;

import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;

  public void registerMenu(MenuCreateDto menuCreateDto) {
    Store findStore = storeRepository.findById(menuCreateDto.getStoreId()).orElseThrow(
        StoreNotFoundException::new);

    Menu menu = Menu.builder()
        .store(findStore)
        .name(menuCreateDto.getMenuName())
        .price(menuCreateDto.getPrice())
        .description(menuCreateDto.getDescription())
        .photo(menuCreateDto.getPhoto())
        .build();

    menuRepository.save(menu);
  }
}
