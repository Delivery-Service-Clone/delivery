package com.example.delivery.domain.pay.repository;

import com.example.delivery.domain.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {}
