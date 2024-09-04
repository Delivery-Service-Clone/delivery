package com.example.delivery.domain.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.delivery.domain.order.dto.CartItemDTO;
import com.example.delivery.domain.order.service.CartService;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.global.result.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

  @Mock
  private CartService cartService;

  @InjectMocks
  private CartController cartController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("장바구니에 메뉴 등록")
  void registerMenuInCart() throws Exception {
    // Given
    Member member = Member.builder().email("test@email.com").build();
    ReflectionTestUtils.setField(member, "id", 1L);
    CartItemDTO cartItemDTO = new CartItemDTO("new Item", 7000L, 1L, 1L, 2L);

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/carts")
                .contentType("application/json")
                .principal(() -> member.getEmail())
                .content(objectMapper.writeValueAsString(cartItemDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.MENU_IN_CART_SUCCESS.getCode()));
  }

  @Test
  @DisplayName("장바구니 전체 삭제")
  void deleteAllMenuInCart() throws Exception {
    // Given
    Member member = Member.builder().email("test@email.com").build();
    ReflectionTestUtils.setField(member, "id", 1L);

    // When & Then
    mockMvc
        .perform(
            delete("/api/v1/carts")
                .principal(() -> member.getEmail()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.CART_DELETE_SUCCESS.getCode()));
  }
}
