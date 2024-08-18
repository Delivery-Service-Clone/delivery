package com.example.delivery.domain.menu.service;

import com.example.delivery.domain.menu.dto.MenuDto;
import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.exception.MenuNotFoundException;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.exception.StoreNotFoundException;
import com.example.delivery.domain.store.repository.StoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;

  public void registerMenu(MenuCreateDto menuCreateDto) {
    Store findStore =
        storeRepository
            .findById(menuCreateDto.getStoreId())
            .orElseThrow(StoreNotFoundException::new);

    Menu menu =
        Menu.builder()
            .store(findStore)
            .name(menuCreateDto.getMenuName())
            .price(menuCreateDto.getPrice())
            .description(menuCreateDto.getDescription())
            .photo(menuCreateDto.getPhoto())
            .build();

    menuRepository.save(menu);
  }

  public List<MenuDto> getMenusByStoreId(Long storeId) {

    Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

    List<Menu> menus = menuRepository.findByStore(store);

    if (menus.isEmpty()) {
      throw new MenuNotFoundException();
    }

    List<MenuDto> menuDtos =
        menus.stream()
            .map(
                menu ->
                    MenuDto.builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .description(menu.getDescription())
                        .photo(menu.getPhoto())
                        .build())
            .collect(Collectors.toList());

    return menuDtos;
  }

  public Menu getMenuByStoreIdAndMenuId(Long storeId, Long menuId) {
    Store store = storeRepository.findByStoreId(storeId).orElseThrow(StoreNotFoundException::new);

    Menu menu = menuRepository.findMenuByStoreAndMenuId(store, menuId)
        .orElseThrow(MenuNotFoundException::new);

    return menu;
  }
}
