package ru.rstdv.monitoringservice.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.rstdv.monitoringservice"})
@Import({org.springdoc.core.configuration.SpringDocConfiguration.class, org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration.class,
        org.springdoc.webmvc.ui.SwaggerConfig.class,
        org.springdoc.core.properties.SwaggerUiConfigProperties.class,
        org.springdoc.core.properties.SwaggerUiOAuthProperties.class,
        org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class})
public class SpringFoxConfig {
    @Bean
    public GroupedOpenApi dafault() {
        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan("ru.rstdv.monitoringservice")
                .pathsToMatch("/").build();
    }
}



