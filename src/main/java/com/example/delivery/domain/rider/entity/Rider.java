package com.example.delivery.domain.rider.entity;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "riders")
public class Rider extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "rider", orphanRemoval = true, cascade = CascadeType.ALL)
  private Order order;

  @Column(nullable = false, length = 45)
  private String name;

  @Column(nullable = false, length = 45)
  private String phone;

  @Column(nullable = false, length = 45)
  private String address;

  @Column(nullable = false, length = 255)
  private String fcmToken;
}
