package ru.rstdv.monitoringservice.util.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rstdv.monitoringservice.config.YamlPropertySourceFactory;
import ru.rstdv.monitoringservice.util.IntegrationTestDataSourceConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.yaml", factory = YamlPropertySourceFactory.class)
@ContextConfiguration(classes = IntegrationTestDataSourceConfig.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IT {
}
