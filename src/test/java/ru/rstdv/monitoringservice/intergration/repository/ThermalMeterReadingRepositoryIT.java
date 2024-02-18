package ru.rstdv.monitoringservice.intergration.repository;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;


import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(classes = {
        ThermalMeterReadingRepositoryImpl.class,
        UserRepositoryImpl.class
})
public class ThermalMeterReadingRepositoryIT extends IntegrationTestBase {
    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepository;
    private final UserRepository userRepository;

//    @BeforeEach
//    void setUp() {
//         connectionProvider = new TestConnectionProvider(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//        LiquibaseUtil.start(connectionProvider);
//        thermalMeterReadingRepository = new ThermalMeterReadingRepositoryImpl(connectionProvider);
//        userRepository = new UserRepositoryImpl(connectionProvider);
//    }

//    @AfterEach
//    void clear() {
//       LiquibaseUtil.dropAll();
//    }

    @DisplayName("save")
    @Test
    void save() {
        var user = userRepository.findById(2L);
        var res1 = thermalMeterReadingRepository.save(createThermalMeterReading(user.get().getId()));
        var res2 = thermalMeterReadingRepository.save(createThermalMeterReading(user.get().getId()));
        var res3 = thermalMeterReadingRepository.save(createThermalMeterReading(user.get().getId()));
        var res4 = thermalMeterReadingRepository.save(createThermalMeterReading(user.get().getId()));

        var meterReadings = thermalMeterReadingRepository.findAll();

        assertThat(meterReadings).hasSize(8);

        List<Long> ids = meterReadings.stream()
                .map(ThermalMeterReading::getId)
                .toList();

        assertThat(ids).contains(
                res1.getId(), res2.getId(),
                res3.getId(), res4.getId()
        );
    }

    @DisplayName("find actual by user id")
    @Test
    void findActualByUserId() {
        var actual = thermalMeterReadingRepository.findActualByUserId(2L);

        assertThat(actual).isPresent();
        assertThat(actual.get().getGigaCalories()).isEqualTo(678F);
        assertThat(actual.get().getMeterReadingDate().getMonth()).isEqualTo(5);
        assertThat(actual.get().getMeterReadingDate().getMonthDay()).isEqualTo(20);

    }


    @DisplayName("find all by user id")
    @Test
    void findAllByUserId() {
        var meters = thermalMeterReadingRepository.findAllByUserId(2L);
        assertThat(meters).hasSize(3);
    }

    @DisplayName("find by month and user id should return not empty optional")
    @Test
    void findByMonthAndUserId_should_return_not_empty_optional() {
        var maybeResult = thermalMeterReadingRepository.
                findByMonthAndUserId(new MonthFilterImpl(3), 2L);

        assertThat(maybeResult).isNotEmpty();

    }

    @DisplayName("find by month and user id should return empty optional")
    @Test
    void findByMonthAndUserId_should_return_empty_optional() {
        var maybeResult = thermalMeterReadingRepository.
                findByMonthAndUserId(new MonthFilterImpl(2), 2L);

        assertThat(maybeResult).isEmpty();

    }

    private ThermalMeterReading createThermalMeterReading(Long userId) {

        return ThermalMeterReading.builder()
                .userId(userId)
                .gigaCalories(123F)
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
