package com.example.delivery.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequest {

  @NotBlank(message = "Email을 입력해주세요")
  private String email;

  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;
}
