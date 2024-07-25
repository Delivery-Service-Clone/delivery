package com.example.delivery.domain.user.example;

import com.example.delivery.domain.user.entity.Owner;
import com.example.delivery.domain.user.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

  private final OwnerRepository ownerRepository;

  public void registerUser(OwnerDto.Request request) {
    Owner user = Owner.builder()
        .name(request.getName())
        .password(request.getPassword())
        .phone(request.getPhone())
        .email(request.getEmail())
        .address(request.getAddress())
        .build();

    ownerRepository.save(user);
  }
}
