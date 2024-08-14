package com.example.delivery.domain.rider.service;

import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.example.delivery.domain.rider.dto.RiderCreateDto;
import com.example.delivery.domain.rider.dto.RiderDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.repository.RiderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

  private final RiderRepository riderRepository;
  private final DeliveryDao deliveryDao;
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
    deliveryDao.registerRiderWhenStartWork(riderDto);
  }

  public void updateRiderStatusToDelivering(RiderDto riderDto) {

    deliveryDao.updateRiderStatusToDelivering(riderDto);
  }

  public void deleteStandbyRiderWhenStopWork(RiderDto riderDto) {

    Rider rider = riderRepository.findByName(riderDto.getName());

    redisTemplate.opsForHash().delete((rider.getAddress()), String.valueOf(rider.getId()));
  }

  public List<String> loadOrderList(String riderAddress) {
    return deliveryDao.selectOrderList(riderAddress);
  }
}
