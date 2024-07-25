package com.example.delivery.domain.store.entity;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.user.entity.Owner;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stores")
public class Store extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private Owner owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(
      mappedBy = "store",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Menu> menus;

  @OneToMany(
      mappedBy = "store",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Order> orders;

  @Column(nullable = false, length = 45)
  private String name;

  @Column(nullable = false, length = 45)
  private String phone;

  @Column(nullable = false, length = 45)
  private String address;

  @Column(nullable = false, length = 45)
  private String openStatus;

  @Column(nullable = false, length = 45)
  private String introduction;

  @Builder
  public Store(Owner owner, Category category,
      String name, String phone, String address, String openStatus, String introduction) {
    this.owner = owner;
    this.category = category;
    this.name = name;
    this.phone = phone;
    this.address = address;
    this.openStatus = openStatus;
    this.introduction = introduction;
  }
}
