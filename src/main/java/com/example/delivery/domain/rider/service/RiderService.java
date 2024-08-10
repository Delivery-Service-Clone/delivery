package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.rider.dto.RiderCreateDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderService {

  private final RiderRepository riderRepository;

  @Transactional
  public void registerRider(RiderCreateDto riderCreateDto) {

    Rider rider = Rider.builder()
        .name(riderCreateDto.getName())
        .phone(riderCreateDto.getPhone())
        .address(riderCreateDto.getAddress())
        .build();

    riderRepository.save(rider);
  }
}