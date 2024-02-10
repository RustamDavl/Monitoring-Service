package ru.rstdv.monitoringservice.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class IntegrationTestBase {

    protected ConnectionProvider connectionProvider;
    protected static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0")
            .withInitScript("init.sql");

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }
}
