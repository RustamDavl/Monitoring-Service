package ru.rstdv.monitoringservice.util;

import liquibase.integration.spring.SpringLiquibase;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestDataSourceConfig {
    @Bean
    DataSource dataSource(@Value("${spring.datasource.url}") String url,
                          @Value("${spring.datasource.password}") String password,
                          @Value("${spring.datasource.username}") String user) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("/db/changelog/db.changelog-master.yaml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
