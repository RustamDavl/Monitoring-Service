package ru.rstdv.monitoringservice.intergration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.*;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.service.*;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ContextConfiguration(classes = {
        AuditServiceImpl.class,
        AuditRepositoryImpl.class,
        AuditMapperImpl.class,
        UserRepositoryImpl.class,
        ThermalMeterMapperImpl.class,
        UserMapperImpl.class,
        ThermalMeterReadingRepositoryImpl.class,
        ThermalMeterReadingServiceImpl.class
})
public class ThermalMeterReadingServiceIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ThermalMeterMapper thermalMeterMapper;

    private final UserMapper userMapper;

    private final AuditMapper auditMapper;
    private final MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepository;
    private final MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> thermalMeterReadingService;

//    @BeforeEach
//    void setUp() {
//        connectionProvider = new TestConnectionProvider(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//        LiquibaseUtil.start(connectionProvider);
//        userRepository = new UserRepositoryImpl(connectionProvider);
//        thermalMeterReadingRepository = new ThermalMeterReadingRepositoryImpl(connectionProvider);
//
//        auditService = new AuditServiceImpl(new AuditRepositoryImpl(connectionProvider), auditMapper);
//        thermalMeterReadingService = new ThermalMeterReadingServiceImpl(thermalMeterReadingRepository, userRepository, thermalMeterMapper, auditService);
//    }
//
//    @AfterEach
//    void clear() {
//        LiquibaseUtil.dropAll();
//    }


    @DisplayName("save should pass")
    @Test
    void save_should_pass() {
        var user = userRepository.findById(2L).get();

        CreateUpdateThermalMeterReadingDto request = new CreateUpdateThermalMeterReadingDto(
                user.getId().toString(),
                "500.0"
        );
        var savedThermalMeterReading = thermalMeterReadingService.save(request);

        assertThat(savedThermalMeterReading.userId()).isEqualTo(user.getId().toString());
        assertThat(savedThermalMeterReading.gigaCalories()).isEqualTo(request.gigaCalories());
    }

    @DisplayName("save should throw UserNotFoundException")
    @Test
    void save_should_throw_UserNotFoundException() {

        var createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(
                "30",
                "123.4"
        );
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> thermalMeterReadingService.save(createUpdateThermalMeterReadingDto));
    }

    @DisplayName("find actual by user id should pass")
    @Test
    void findActualByUserId_should_pass() {
        var actual = thermalMeterReadingService.findActualByUserId(2L);
        assertThat(actual.gigaCalories()).isEqualTo("678.0");
        assertThat(actual.dateOfMeterReading().getYear().getValue()).isEqualTo(2023);
        assertThat(actual.dateOfMeterReading().getMonth()).isEqualTo(5);
        assertThat(actual.dateOfMeterReading().getMonthDay()).isEqualTo(20);
    }

    @DisplayName("find actual by user id should throw UserNotFoundException")
    @Test
    void findActualByUserId_should_throw_UserNotFoundException() {
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> thermalMeterReadingService.findActualByUserId(30L));
    }

    @DisplayName("find all by user id")
    @Test
    void findAllByUserId() {
        var thermalMeterReadings = thermalMeterReadingService.findAllByUserId(2L);
        assertThat(thermalMeterReadings).hasSize(3);
    }

    @DisplayName("find by month and user id should pass")
    @Test
    void findByMonthAndUserId_should_pass() {
        var actual = thermalMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(4), 2L);
        assertThat(actual.gigaCalories()).isEqualTo("345.0");
        assertThat(actual.dateOfMeterReading().getYear().getValue()).isEqualTo(2023);
        assertThat(actual.dateOfMeterReading().getMonth()).isEqualTo(4);
        assertThat(actual.dateOfMeterReading().getMonthDay()).isEqualTo(23);
    }

    @DisplayName("find by month and user id should throw MeterReadingNotFound")
    @Test
    void findByMonthAndUserId_should_throw_MeterReadingNotFound() {
        org.junit.jupiter.api.Assertions.assertThrows(MeterReadingNotFoundException.class,
                () -> thermalMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(6), 2L));
    }
}
