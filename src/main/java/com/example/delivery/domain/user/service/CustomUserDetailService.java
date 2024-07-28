package com.example.delivery.domain.user.service;

import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.domain.user.repository.MemberRepository;
import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    if (email == null || email.equals("")) {
      throw new BusinessException(ErrorCode.USER_NOT_FOUND_ERROR);
    }

    Optional<Member> member = memberRepository.findByEmail(email);
    if (member.isPresent()) {
      return member.get();
    }
    throw new BusinessException(ErrorCode.USER_NOT_FOUND_ERROR);
  }
}
