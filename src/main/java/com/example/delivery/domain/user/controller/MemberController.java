package com.example.delivery.domain.user.controller;

import com.example.delivery.domain.user.dto.MemberDto;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;
import com.example.delivery.global.result.ResultCode;
import com.example.delivery.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member")
public class MemberController {

  @GetMapping("/me")
  public ResponseEntity<ResultResponse> getMember(@AuthenticationPrincipal Member member) {
    if (member == null) {
      throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
    }

    MemberDto memberDto = MemberDto.builder()
        .email(member.getEmail())
        .name(member.getName())
        .phone(member.getPhone())
        .address(member.getAddress())
        .build();

    return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_SUCCESS, memberDto));
  }
}
