package com.example.delivery.domain.user.entity;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<Order> orders;

  @Column(nullable = false, unique = true, length = 45)
  private String email;

  @Column(nullable = false, length = 45)
  private String name;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 45)
  private String phone;

  @Column(nullable = false, length = 45)
  private String address;
}
