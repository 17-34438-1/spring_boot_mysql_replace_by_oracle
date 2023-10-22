package com.datasoft.IgmMis.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean(name = "primaryDb")
    @Primary
    @ConfigurationProperties(prefix = "spring.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplatePrimary")
    public JdbcTemplate jdbcTemplatePrimary(@Qualifier("primaryDb") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryDb")
    @ConfigurationProperties(prefix = "spring.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateSecondary")
    public JdbcTemplate jdbcTemplateSecondary(@Qualifier("secondaryDb") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "OracleDb")
    @ConfigurationProperties(prefix = "datasource.oracle")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateOracle")
    public JdbcTemplate jdbcTemplateOracle(@Qualifier("OracleDb") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }



}
