package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {}
