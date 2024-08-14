package com.example.delivery.domain.store.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.delivery.domain.store.dto.StoreDto;
import com.example.delivery.domain.store.dto.request.StoreCreateDto;
import com.example.delivery.domain.store.entity.Category;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.service.StoreService;
import com.example.delivery.domain.user.entity.Owner;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // MockitoExtension 적용
class StoreControllerTest {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock private StoreService storeService;

  @InjectMocks private StoreController storeController;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
    this.objectMapper = new ObjectMapper(); // ObjectMapper 직접 생성
  }

  @Test
  @DisplayName("모든 가게 목록을 성공적으로 조회한다")
  void testGetStores() throws Exception {
    // Given
    StoreDto storeDto1 =
        new StoreDto(
            1L,
            "Burger King",
            "456 Elm St",
            "123-456-7890",
            StoreStatus.STORE_OPEN,
            "Best burgers",
            Category.BURGER);
    StoreDto storeDto2 =
        new StoreDto(
            2L,
            "Korean BBQ",
            "123 Main St",
            "111-111-1111",
            StoreStatus.STORE_OPEN,
            "Delicious Korean BBQ",
            Category.KOREAN);
    List<StoreDto> stores = Arrays.asList(storeDto1, storeDto2);

    when(storeService.getAllStores()).thenReturn(stores);

    // When & Then
    mockMvc
        .perform(get("/api/v1/stores/list").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(2))
        .andExpect(jsonPath("$.data[0].storeName").value("Burger King"))
        .andExpect(jsonPath("$.data[1].storeName").value("Korean BBQ"));
  }

  @Test
  @DisplayName("새로운 가게를 성공적으로 등록한다")
  void testRegisterStore() throws Exception {
    // Given
    StoreCreateDto storeCreateDto =
        new StoreCreateDto(
            "Burger King",
            "123-456-7890",
            "456 Elm St",
            StoreStatus.STORE_OPEN,
            "Best burgers",
            Category.BURGER);

    Owner owner =
        Owner.builder()
            .email("owner@example.com")
            .name("John Doe")
            .password("securepassword")
            .phone("123-456-7890")
            .address("123 Main St")
            .build();

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeCreateDto))
                .principal(() -> owner.getEmail())) // Assuming a valid Owner is authenticated
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("식당이 정상적으로 등록되었습니다."));
  }

  @Test
  @DisplayName("가게 상태를 성공적으로 변경한다")
  void testChangeStoreStatus() throws Exception {
    // Given
    Long storeId = 1L;
    StoreStatus newStatus = StoreStatus.STORE_CLOSE;

    // When & Then
    mockMvc
        .perform(
            put("/api/v1/stores/{storeId}/status", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStatus)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("식당 상태가 정상적으로 변경되었습니다."));
  }
}
