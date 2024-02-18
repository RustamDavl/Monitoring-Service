package ru.rstdv.monitoringservice.config;

import liquibase.integration.spring.SpringLiquibase;
import org.postgresql.ds.PGSimpleDataSource;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;


@ComponentScan("ru.rstdv.monitoringservice")
@Configuration
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
@EnableWebMvc
@EnableAspectJAutoProxy
public class ContextConfiguration {
    @Bean
    SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }

    @Bean
    DataSource dataSource(@Value("${spring.datasource.url}") String url,
                          @Value("${spring.datasource.password}") String password,
                          @Value("${spring.datasource.username}") String username,
                          @Value("${spring.datasource.dbcp2.default-schema}") String currentSchema) {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(url);
        dataSource.setPassword(password);
        dataSource.setUser(username);
        dataSource.setCurrentSchema(currentSchema);
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("/db/changelog/db.changelog-master.yaml");
        liquibase.setDefaultSchema("monitoring_service");
        liquibase.setLiquibaseSchema("liquibase");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
