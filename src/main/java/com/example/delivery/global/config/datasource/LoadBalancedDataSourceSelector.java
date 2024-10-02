package com.example.delivery.global.config.datasource;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancedDataSourceSelector {

  // 슬레이브 개수를 정의
  private static final int SLAVE_COUNT = 3;

  // 현재 슬레이브 인덱스를 저장하는 AtomicInteger
  private static final AtomicInteger counter = new AtomicInteger(0);

  public static DataSourceType selectSlaveDataSource() {
    // 라운드로빈 방식으로 슬레이브 데이터 소스를 선택
    int index = counter.getAndIncrement() % SLAVE_COUNT + 1;
    return DataSourceType.valueOf("SLAVE" + index);
  }
}