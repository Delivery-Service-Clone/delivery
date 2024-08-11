package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.rider.dto.RiderCreateDto;
import com.example.delivery.domain.rider.dto.RiderDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

  private final RiderRepository riderRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  public void registerRider(RiderCreateDto riderCreateDto) {

    Rider rider =
        Rider.builder()
            .name(riderCreateDto.getName())
            .phone(riderCreateDto.getPhone())
            .address(riderCreateDto.getAddress())
            .build();

    riderRepository.save(rider);
  }

  public void registerStandbyRiderWhenStartWork(RiderDto riderDto) {

    Rider rider = riderRepository.findByName(riderDto.getName());

    String hashKey = riderDto.getAddress(); // 해시 키 (예: "riders"라는 해시 안에 여러 라이더 정보를 저장)
    String field = String.valueOf(rider.getId()); // 필드 (예: 라이더의 고유 ID를 필드로 사용)

    redisTemplate.opsForHash().put(hashKey, field, "STANDBY");
  }

  public void updateRiderStatusToDelivering(Long riderId, String address) {

    String field = String.valueOf(riderId);

    redisTemplate.opsForHash().put(address, field, "DELIVERING");
  }

  public void deleteStandbyRiderWhenStopWork(RiderDto riderDto) {

    Rider rider = riderRepository.findByName(riderDto.getName());

    redisTemplate.opsForHash()
        .delete((rider.getAddress()), String.valueOf(rider.getId()));
  }
}
