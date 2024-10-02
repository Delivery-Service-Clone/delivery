package com.example.delivery.global.config.datasource;

public class DataSourceContextHolder {

  // ThreadLocal을 사용하여 각 스레드마다 별도의 데이터 소스 키를 관리
  private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

  // 현재 스레드에 데이터 소스 키를 설정
  public static void setDataSourceKey(DataSourceType key) {
    contextHolder.set(key);
  }

  // 현재 스레드의 데이터 소스 키를 가져옴
  public static DataSourceType getDataSourceKey() {
    return contextHolder.get();
  }

  // 현재 스레드의 데이터 소스 키를 제거 (정리)
  public static void clearDataSourceKey() {
    contextHolder.remove();
  }
}
