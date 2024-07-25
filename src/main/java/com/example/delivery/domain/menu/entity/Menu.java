package com.example.delivery.domain.menu.entity;

import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.entity.OrderMenuOption;
import com.example.delivery.domain.store.entity.Store;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menus")
public class Menu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderMenu> orderMenus;

  @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MenuOption> menuOptions;

  @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderMenuOption> orderMenuOptions;

  @Column(nullable = false, length = 45)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false, length = 255)
  private String description;

  @Column(nullable = false, length = 255)
  private String photo;
}
