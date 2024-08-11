package com.example.delivery.domain.rider.repository;

import com.example.delivery.domain.rider.entity.Rider;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
  List<Rider> findByAddress(String address); // 특정 주소로 라이더 검색

  Rider findByName(String name);
}
