package ru.rstdv.monitoringservice.unit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.ThermalMeterReadingRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

public class ThermalMeterReadingRepositoryTest {

    private final MeterReadingRepository<ThermalMeterReading> repository = ThermalMeterReadingRepositoryImpl.getInstance();

    @AfterEach
    void clearDataBase() {
        ThermalMeterReadingRepositoryImpl.clearDataBase();
    }

    @Test
    void save() {
        var thermalMeterReading1 = repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 1, 23, 10, 45)));
        var thermalMeterReading2 = repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 2, 21, 11, 45)));
        var thermalMeterReading3 = repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 3, 22, 15, 45)));
        var thermalMeterReading4 = repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 4, 23, 13, 45)));
        var thermalMeterReading5 = repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 5, 24, 12, 45)));
        var thermalMeterReading6 = repository.save(createThermalMeterReading(createUser(4L), LocalDateTime.of(2024, 6, 25, 13, 45)));
        var meterReadings = repository.findAll();

        assertThat(meterReadings).hasSize(6);

        List<Long> ids = meterReadings.stream()
                .map(ThermalMeterReading::getId)
                .toList();

        assertThat(ids).contains(
                thermalMeterReading1.getId(), thermalMeterReading2.getId(), thermalMeterReading3.getId(),
                thermalMeterReading4.getId(), thermalMeterReading5.getId(), thermalMeterReading6.getId()
        );
    }

    @Test
    void findActualByUserId() {
        var userId = 1L;
        repository.save(createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 1, 23, 10, 45)));
        repository.save(createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 2, 21, 11, 45)));
        repository.save(createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 3, 22, 15, 45)));
        repository.save(createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 4, 23, 13, 45)));
        repository.save(createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 5, 24, 12, 45)));
        repository.save(createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 6, 25, 13, 45)));

        var actualResult = repository.findActualByUserId(userId).get();

        var expectedResult = createThermalMeterReading(createUser(userId), LocalDateTime.of(2024, 6, 25, 13, 45));
        expectedResult.setId(6L);
        assertThat(actualResult.getDateOfMeterReading()).isEqualTo(expectedResult.getDateOfMeterReading());
        assertThat(actualResult.getUser().getEmail()).isEqualTo(expectedResult.getUser().getEmail());
        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getGigaCalories()).isEqualTo(expectedResult.getGigaCalories());

    }


    @Test
    void findAllByUserId() {
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 1, 23, 10, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 2, 21, 11, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 3, 22, 15, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 4, 23, 13, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 4, 23, 13, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 5, 24, 12, 45)));
        repository.save(createThermalMeterReading(createUser(4L), LocalDateTime.of(2024, 6, 25, 13, 45)));

        var thermalReadings = repository.findAllByUserId(2L);
        assertThat(thermalReadings).hasSize(4);
    }

    @Test
    void findByMonthAndUserId_should_return_not_empty_optional() {
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 1, 23, 10, 45)));
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 2, 21, 11, 45)));
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 3, 22, 15, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 1, 23, 10, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 2, 21, 11, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 3, 22, 15, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 4, 23, 13, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 5, 24, 12, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 6, 25, 13, 45)));

        var actualResult = repository.findByMonthAndUserId(new MonthFilterImpl(4), 3L);
        assertThat(actualResult).isNotEmpty();

    }

    @Test
    void findByMonthAndUserId_should_return_empty_optional() {
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 1, 23, 10, 45)));
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 2, 21, 11, 45)));
        repository.save(createThermalMeterReading(createUser(1L), LocalDateTime.of(2024, 3, 22, 15, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 1, 23, 10, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 2, 21, 11, 45)));
        repository.save(createThermalMeterReading(createUser(2L), LocalDateTime.of(2024, 3, 22, 15, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 4, 23, 13, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 5, 24, 12, 45)));
        repository.save(createThermalMeterReading(createUser(3L), LocalDateTime.of(2024, 6, 25, 13, 45)));

        var actualResult = repository.findByMonthAndUserId(new MonthFilterImpl(4), 1L);
        assertThat(actualResult).isEmpty();

    }

    private User createUser(Long id) {
        var email = id + "@gmail.com";
        return User.builder()
                .id(id)
                .firstname("Vi")
                .email(email)
                .password("pass")
                .personalAccount("999999999")
                .address(
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build()
                )
                .role(Role.USER)
                .build();
    }

    private ThermalMeterReading createThermalMeterReading(User user, LocalDateTime localDateTime) {
        return ThermalMeterReading.builder()
                .user(user)
                .dateOfMeterReading(localDateTime)
                .gigaCalories(100F)
                .build();
    }

}
