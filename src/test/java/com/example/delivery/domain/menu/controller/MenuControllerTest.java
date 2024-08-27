package com.example.delivery.domain.menu.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.delivery.domain.menu.dto.MenuDto;
import com.example.delivery.domain.menu.dto.request.MenuCreateDto;
import com.example.delivery.domain.menu.service.MenuService;
import com.example.delivery.global.result.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

  @Mock
  private MenuService menuService;

  @InjectMocks
  private MenuController menuController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    // MockMvc 및 ObjectMapper 초기화
    this.mockMvc = MockMvcBuilders.standaloneSetup(menuController).build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("메뉴를 성공적으로 등록한다")
  void registerMenu() throws Exception {
    // Given
    MenuCreateDto menuCreateDto =
        MenuCreateDto.builder()
            .storeId(1L)
            .menuName("Burger")
            .price(10000L)
            .description("Delicious burger")
            .photo("burger.jpg")
            .build();

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/menus")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(menuCreateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.MENU_REGISTRATION_SUCCESS.getCode()));
  }

  @Test
  @DisplayName("가게 ID를 기준으로 메뉴를 성공적으로 조회한다")
  void getMenusByStoreId() throws Exception {
    // Given
    Long storeId = 1L;
    List<MenuDto> menus =
        List.of(
            MenuDto.builder()
                .id(1L)
                .name("Burger")
                .price(10000L)
                .description("Delicious burger")
                .photo("burger.jpg")
                .build(),
            MenuDto.builder()
                .id(2L)
                .name("Fries")
                .price(5000L)
                .description("Crispy fries")
                .photo("fries.jpg")
                .build());

    given(menuService.getMenusByStoreId(storeId)).willReturn(menus);

    // When & Then
    mockMvc
        .perform(get("/api/v1/menus/{storeId}", storeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.MENU_GET_SUCCESS.getCode()))
        .andExpect(jsonPath("$.data.length()").value(2))
        .andExpect(jsonPath("$.data[0].name").value("Burger"))
        .andExpect(jsonPath("$.data[1].name").value("Fries"));
  }
}
