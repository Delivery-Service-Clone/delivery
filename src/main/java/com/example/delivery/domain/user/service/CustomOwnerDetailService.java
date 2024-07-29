package com.example.delivery.domain.user.service;

import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
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
public class CustomOwnerDetailService implements UserDetailsService {

  private final OwnerRepository ownerRepository;

  @Override
  public UserDetails loadUserByUsername(String ownerEmail) throws UsernameNotFoundException {
    if (ownerEmail == null || ownerEmail.equals("")) {
      throw new BusinessException(ErrorCode.OWNER_NOT_FOUND_ERROR);
    }

    Optional<Owner> owner = ownerRepository.findByEmail(ownerEmail);
    if (owner.isPresent()) {
      return owner.get();
    }
    throw new BusinessException(ErrorCode.USER_NOT_FOUND_ERROR);
  }
}
