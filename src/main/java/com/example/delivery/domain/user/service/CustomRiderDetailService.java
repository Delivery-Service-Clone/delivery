package com.example.delivery.domain.user.service;

import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.repository.RiderRepository;
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
public class CustomRiderDetailService implements UserDetailsService {

  private final RiderRepository riderRepository;

  @Override
  public UserDetails loadUserByUsername(String riderEmail) throws UsernameNotFoundException {
    if (riderEmail == null || riderEmail.equals("")) {
      throw new BusinessException(ErrorCode.INPUT_INVALID_VALUE);
    }

    Optional<Rider> rider = riderRepository.findByEmail(riderEmail);
    if (rider.isPresent()) {
      return rider.get();
    }
    throw new BusinessException(ErrorCode.RIDER_NOT_FOUND_ERROR);
  }
}
