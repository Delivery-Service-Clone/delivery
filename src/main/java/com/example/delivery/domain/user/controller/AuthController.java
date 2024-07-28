package com.example.delivery.domain.user.controller;

import static com.example.delivery.global.result.ResultCode.CHECK_EMAIL_BAD;
import static com.example.delivery.global.result.ResultCode.CHECK_EMAIL_GOOD;
import static com.example.delivery.global.result.ResultCode.LOGIN_SUCCESS;
import static com.example.delivery.global.result.ResultCode.USER_REGISTRATION_SUCCESS;

import com.example.delivery.domain.user.dto.MemberLoginRequest;
import com.example.delivery.domain.user.dto.MemberRegisterRequest;
import com.example.delivery.domain.user.dto.OwnerLoginRequest;
import com.example.delivery.domain.user.dto.OwnerRegisterRequest;
import com.example.delivery.domain.user.service.AuthService;
import com.example.delivery.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService AuthService;

  @GetMapping("/member/checkid")
  public ResponseEntity<ResultResponse> checkMemberEmail(@RequestBody String email) {
    final boolean check = AuthService.checkMemberEmail(email);
    if (check) {
      return ResponseEntity.ok(ResultResponse.of(CHECK_EMAIL_GOOD, true));
    } else {
      return ResponseEntity.ok(ResultResponse.of(CHECK_EMAIL_BAD, false));
    }
  }

  @GetMapping("/owner/checkid")
  public ResponseEntity<ResultResponse> checkOwnerEmail(@RequestBody String email) {
    final boolean check = AuthService.checkOwnerEmail(email);
    if (check) {
      return ResponseEntity.ok(ResultResponse.of(CHECK_EMAIL_GOOD, true));
    } else {
      return ResponseEntity.ok(ResultResponse.of(CHECK_EMAIL_BAD, false));
    }
  }

  @PostMapping(value = "/member/register")
  public ResponseEntity<ResultResponse> registerMember(
      @Valid @RequestBody MemberRegisterRequest request) {

    AuthService.registerMember(request);
    return ResponseEntity.ok(ResultResponse.of(USER_REGISTRATION_SUCCESS));
  }

  @PostMapping(value = "/owner/register")
  public ResponseEntity<ResultResponse> registerOwner(
      @Valid @RequestBody OwnerRegisterRequest request) {

    AuthService.registerOwner(request);
    return ResponseEntity.ok(ResultResponse.of(USER_REGISTRATION_SUCCESS));
  }

  @PostMapping("/member/login")
  public ResponseEntity<ResultResponse> loginMember(
      @Valid @RequestBody MemberLoginRequest request) {
    String token = AuthService.Memberlogin(request);
    return ResponseEntity.ok(ResultResponse.of(LOGIN_SUCCESS, token));
  }

  @PostMapping("/owner/login")
  public ResponseEntity<ResultResponse> loginOwner(
      @Valid @RequestBody OwnerLoginRequest request) {
    String token = AuthService.Ownerlogin(request);
    return ResponseEntity.ok(ResultResponse.of(LOGIN_SUCCESS, token));
  }

  @PostMapping("/test")
  public String test() {
    return "success";
  }
}
