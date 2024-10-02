package com.example.delivery.global.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

  private static final String MASTER_DATASOURCE = "masterDataSource";
  private static final String SLAVE1_DATASOURCE = "slave1DataSource";
  private static final String SLAVE2_DATASOURCE = "slave2DataSource";
  private static final String SLAVE3_DATASOURCE = "slave3DataSource";

  @Bean(MASTER_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
  public DataSource masterDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(SLAVE1_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.slave1.hikari")
  public DataSource slave1DataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(SLAVE2_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.slave2.hikari")
  public DataSource slave2DataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean(SLAVE3_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.datasource.slave3.hikari")
  public DataSource slave3DataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  @DependsOn({MASTER_DATASOURCE, SLAVE1_DATASOURCE, SLAVE2_DATASOURCE, SLAVE3_DATASOURCE})
  public DataSource routingDataSource(
      @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
      @Qualifier(SLAVE1_DATASOURCE) DataSource slave1DataSource,
      @Qualifier(SLAVE2_DATASOURCE) DataSource slave2DataSource,
      @Qualifier(SLAVE3_DATASOURCE) DataSource slave3DataSource) {

    ReplicationRoutingDataSource routingDatasource = new ReplicationRoutingDataSource();

    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put(DataSourceType.MASTER, masterDataSource);
    dataSourceMap.put(DataSourceType.SLAVE1, slave1DataSource);
    dataSourceMap.put(DataSourceType.SLAVE2, slave2DataSource);
    dataSourceMap.put(DataSourceType.SLAVE3, slave3DataSource);

    routingDatasource.setTargetDataSources(dataSourceMap);
    routingDatasource.setDefaultTargetDataSource(masterDataSource);

    return routingDatasource;
  }

  @Bean
  @DependsOn("routingDataSource")
  public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setDataSource(dataSource);
    em.setPackagesToScan("com.example.delivery.domain");

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.implicit_naming_strategy",
        SpringImplicitNamingStrategy.class.getName());
    properties.put("hibernate.hbm2ddl.auto", "update");
    properties.put("hibernate.show_sql", "true");    // SQL 쿼리 출력
    properties.put("hibernate.format_sql", "true");  // 출력된 SQL 포맷팅
    properties.put("hibernate.use_sql_comments", "true");  // 쿼리에 주석 추가

    em.setJpaPropertyMap(properties);

    return em;
  }

  @Primary
  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);

    return transactionManager;
  }
}
