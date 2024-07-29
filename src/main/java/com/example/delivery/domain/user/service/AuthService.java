package com.example.delivery.domain.user.service;

import static com.example.delivery.global.error.ErrorCode.OWNER_NOT_FOUND_ERROR;
import static com.example.delivery.global.error.ErrorCode.USER_NOT_FOUND_ERROR;

import com.example.delivery.domain.user.dto.MemberLoginRequest;
import com.example.delivery.domain.user.dto.MemberRegisterRequest;
import com.example.delivery.domain.user.dto.OwnerLoginRequest;
import com.example.delivery.domain.user.dto.OwnerRegisterRequest;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.exception.EntityAlreadyExistException;
import com.example.delivery.domain.user.repository.MemberRepository;
import com.example.delivery.domain.user.repository.OwnerRepository;
import com.example.delivery.global.config.security.JwtTokenProvider;
import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;
  private final OwnerRepository ownerRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public boolean checkMemberEmail(String email) {
    return !memberRepository.existsByEmail(email);
  }

  @Transactional(readOnly = true)
  public boolean checkOwnerEmail(String email) {
    return !memberRepository.existsByEmail(email);
  }

  @Transactional
  public void signupMember(MemberRegisterRequest request) {
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new EntityAlreadyExistException();
    }
    try {
      final Member member = convertRegisterRequestToMember(request);
      memberRepository.save(member);
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  public void signupOwner(OwnerRegisterRequest request) {
    if (ownerRepository.existsByEmail(request.getEmail())) {
      throw new EntityAlreadyExistException();
    }
    try {
      final Owner owner = convertRegisterRequestToOwner(request);
      ownerRepository.save(owner);
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Transactional
  public String Memberlogin(MemberLoginRequest request) {
    Member member =
        memberRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException(USER_NOT_FOUND_ERROR));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new BusinessException(ErrorCode.INVALID_PASSWORD);
    }

    String token = jwtTokenProvider.createToken(member.getEmail(), "MEMBER");

    return token;
  }

  @Transactional
  public String Ownerlogin(OwnerLoginRequest request) {
    Owner owner =
        ownerRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException(OWNER_NOT_FOUND_ERROR));

    if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
      throw new BusinessException(ErrorCode.INVALID_PASSWORD);
    }

    String token = jwtTokenProvider.createToken(owner.getEmail(), "OWNER");

    return token;
  }

  private Member convertRegisterRequestToMember(MemberRegisterRequest request) {
    return Member.builder()
        .email(request.getEmail())
        .name(request.getName())
        .password(passwordEncoder.encode(request.getPassword()))
        .phone(request.getPhone())
        .address(request.getAddress())
        .build();
  }

  private Owner convertRegisterRequestToOwner(OwnerRegisterRequest request) {
    return Owner.builder()
        .email(request.getEmail())
        .name(request.getName())
        .password(passwordEncoder.encode(request.getPassword()))
        .phone(request.getPhone())
        .address(request.getAddress())
        .build();
  }
}
