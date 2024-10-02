package com.example.delivery.global.config.aop;

import static com.mysql.cj.conf.PropertyKey.logger;

import com.example.delivery.global.config.datasource.DataSourceContextHolder;
import com.example.delivery.global.config.datasource.DataSourceType;
import com.example.delivery.global.config.datasource.LoadBalancedDataSourceSelector;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Slf4j
@Component
public class DataSourceAspect {

  @Before("execution(* com.example.delivery.domain..*.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional)")
  public void setDataSource(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Transactional transactional = signature.getMethod().getAnnotation(Transactional.class);

    if (transactional != null && transactional.readOnly()) {
      DataSourceType selectedSlave = LoadBalancedDataSourceSelector.selectSlaveDataSource();
      DataSourceContextHolder.setDataSourceKey(selectedSlave);
    } else {
      DataSourceContextHolder.setDataSourceKey(DataSourceType.MASTER);
    }

    log.info("AOP 실행");
  }

  @After("execution(* com.example.delivery.domain..*.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional)")
  public void clearDataSource(JoinPoint joinPoint) {
    DataSourceContextHolder.clearDataSourceKey();
    log.info("AOP 종료");
  }
}