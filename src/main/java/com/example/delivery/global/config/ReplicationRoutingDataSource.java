package com.example.delivery.global.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

  @Override
  public Object determineCurrentLookupKey() {
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    String dataSource = isReadOnly ? "slave" : "master";
    logger.info("Using DataSource: " + dataSource);
    return dataSource;
  }
}
