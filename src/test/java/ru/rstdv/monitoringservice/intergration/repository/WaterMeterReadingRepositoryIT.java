package ru.rstdv.monitoringservice.intergration.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WaterMeterReadingRepositoryIT extends IntegrationTestBase {
    private MeterReadingRepository<WaterMeterReading> waterMeterReadingRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        connectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(connectionProvider);
        waterMeterReadingRepository = new WaterMeterReadingRepositoryImpl(connectionProvider);
        userRepository = new UserRepositoryImpl(connectionProvider);
    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }

    @DisplayName("save")
    @Test
    void save() {
        var user = userRepository.findById(2L);
        var res1 = waterMeterReadingRepository.save(createWaterMeterReading(user.get().getId()));
        var res2 = waterMeterReadingRepository.save(createWaterMeterReading(user.get().getId()));
        var res3 = waterMeterReadingRepository.save(createWaterMeterReading(user.get().getId()));
        var res4 = waterMeterReadingRepository.save(createWaterMeterReading(user.get().getId()));

        var meterReadings = waterMeterReadingRepository.findAll();

        assertThat(meterReadings).hasSize(8);

        List<Long> ids = meterReadings.stream()
                .map(WaterMeterReading::getId)
                .toList();

        assertThat(ids).contains(
                res1.getId(), res2.getId(),
                res3.getId(), res4.getId()
        );
    }

    @DisplayName("find actual by user id")
    @Test
    void findActualByUserId() {
        var actual = waterMeterReadingRepository.findActualByUserId(2L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getColdWater()).isEqualTo(300);
        assertThat(actual.get().getHotWater()).isEqualTo(240);
        assertThat(actual.get().getMeterReadingDate().getMonth()).isEqualTo(5);
        assertThat(actual.get().getMeterReadingDate().getMonthDay()).isEqualTo(20);
    }

    @DisplayName("find all by user id")
    @Test
    void findAllByUserId() {
        var meters = waterMeterReadingRepository.findAllByUserId(2L);
        assertThat(meters).hasSize(3);
    }

    @DisplayName("find by month and user id should return not empty optional")
    @Test
    void findByMonthAndUserId_should_return_not_empty_optional() {
        var maybeResult = waterMeterReadingRepository.
                findByMonthAndUserId(new MonthFilterImpl(3), 2L);

        assertThat(maybeResult).isNotEmpty();
    }

    @DisplayName("find by month and user id should return empty optional")
    @Test
    void findByMonthAndUserId_should_return_empty_optional() {
        var maybeResult = waterMeterReadingRepository.
                findByMonthAndUserId(new MonthFilterImpl(2), 2L);

        assertThat(maybeResult).isEmpty();
    }

    private WaterMeterReading createWaterMeterReading(Long userId) {

        return WaterMeterReading.builder()
                .userId(userId)
                .coldWater(100)
                .hotWater(50)
                .meterReadingDate(
                        MeterReadingDate.builder()
                                .year(Year.of(2024))
                                .month(1)
                                .monthDay(25)
                                .build()
                )
                .build();
    }
}
