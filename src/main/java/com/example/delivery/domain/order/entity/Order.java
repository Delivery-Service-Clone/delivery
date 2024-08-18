package com.example.delivery.domain.order.entity;

import com.example.delivery.domain.pay.entity.Pay;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.Member;
import com.example.delivery.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders") // 테이블 이름 변경
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rider_id")
  private Rider rider;

  @OneToMany(
      mappedBy = "order",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Pay> pays;

  @OneToMany(
      mappedBy = "order",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<OrderMenu> orderMenus;

  @Column(nullable = false, length = 45)
  private String address;

  @Column(nullable = false)
  private Long totalPrice;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @Builder
  public Order(Member member, Store store, OrderStatus orderStatus, String address,
      Long totalPrice) {
    this.member = member;
    this.store = store;
    this.orderStatus = orderStatus;
    this.address = address;
    this.totalPrice = totalPrice;
  }

  public void updateStatus(OrderStatus status) {
    this.orderStatus = status;
  }

  public void updateTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }
}
