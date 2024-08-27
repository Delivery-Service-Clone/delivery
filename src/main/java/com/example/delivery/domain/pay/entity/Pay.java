package com.example.delivery.domain.pay.entity;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pays")
public class Pay extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Enumerated(EnumType.STRING)
  private PayType payType;

  @Column(nullable = false)
  private Long price;

  @Enumerated(EnumType.STRING)
  private PayStatus status;

  @Builder
  public Pay(Order order, PayType payType, Long price, PayStatus status) {
    this.order = order;
    this.payType = payType;
    this.price = price;
    this.status = status;
  }
}
