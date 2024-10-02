package com.example.delivery.global.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    // DataSourceContextHolder에서 설정된 데이터 소스 키를 확인
    DataSourceType dataSourceKey = DataSourceContextHolder.getDataSourceKey();

    // 명시적으로 설정된 데이터 소스가 없으면 트랜잭션의 읽기 전용 여부에 따라 결정
    if (dataSourceKey == null) {
      boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
      dataSourceKey =
          isReadOnly
              ? LoadBalancedDataSourceSelector.selectSlaveDataSource()
              : DataSourceType.MASTER;

      logger.info("Automatically using DataSource: " + dataSourceKey);
    } else {
      logger.info("Explicitly using DataSource: " + dataSourceKey);
    }

    return dataSourceKey;
  }
}
