package com.example.delivery.domain.user.repository;

import com.example.delivery.domain.user.entity.Owner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

  Optional<Owner> findByEmail(String email);

  boolean existsByEmail(String email);
}
