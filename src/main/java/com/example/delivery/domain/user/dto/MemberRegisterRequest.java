package com.example.delivery.domain.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterRequest {

  @NotBlank(message = "Email을 입력해주세요")
  private String email;

  @NotBlank(message = "이름을 입력해주세요")
  private String name;

  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;

  @NotBlank(message = "전화번호를 입력해주세요")
  private String phone;

  @NotBlank(message = "주소를 입력해주세요")
  private String address;
}
