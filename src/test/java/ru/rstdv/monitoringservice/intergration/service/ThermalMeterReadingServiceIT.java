package ru.rstdv.monitoringservice.intergration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.intergration.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.mapper.*;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.service.*;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import static org.assertj.core.api.Assertions.assertThat;

public class ThermalMeterReadingServiceIT extends IntegrationTestBase {

    private UserRepository userRepository;
    private AuditService auditService;
    private ThermalMeterMapper thermalMeterMapper;

    private UserMapper userMapper;
    private MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepository;
    private MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> thermalMeterReadingService;

    @BeforeEach
    void setUp() {
        TestConnectionProvider testConnectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(testConnectionProvider);
        userRepository = new UserRepositoryImpl(testConnectionProvider);
        thermalMeterReadingRepository = new ThermalMeterReadingRepositoryImpl(testConnectionProvider);
        thermalMeterMapper = new ThermalMeterMapperImpl();
        userMapper = new UserMapperImpl();
        auditService = new AuditServiceImpl(new AuditRepositoryImpl(testConnectionProvider), new AuditMapperImpl(userMapper), userRepository);
        thermalMeterReadingService = new ThermalMeterReadingServiceImpl(thermalMeterReadingRepository, userRepository, thermalMeterMapper, auditService);

    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }


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

    @Test
    void save_should_throw_UserNotFoundException() {

        var createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(
                "30",
                "123.4"
        );
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> thermalMeterReadingService.save(createUpdateThermalMeterReadingDto));

    }

    @Test
    void findActualByUserId_should_pass() {
        var actual = thermalMeterReadingService.findActualByUserId(2L);
        assertThat(actual.gigaCalories()).isEqualTo("678.0");
        assertThat(actual.dateOfMeterReading().getYear().getValue()).isEqualTo(2023);
        assertThat(actual.dateOfMeterReading().getMonth()).isEqualTo(5);
        assertThat(actual.dateOfMeterReading().getMonthDay()).isEqualTo(20);
    }

    @Test
    void findActualByUserId_should_throw_UserNotFoundException() {
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> thermalMeterReadingService.findActualByUserId(30L));
    }

    @Test
    void findAllByUserId() {
        var thermalMeterReadings = thermalMeterReadingService.findAllByUserId(2L);
        assertThat(thermalMeterReadings).hasSize(3);
    }

    @Test
    void findByMonthAndUserId_should_pass() {
        var actual = thermalMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(4), 2L);
        assertThat(actual.gigaCalories()).isEqualTo("345.0");
        assertThat(actual.dateOfMeterReading().getYear().getValue()).isEqualTo(2023);
        assertThat(actual.dateOfMeterReading().getMonth()).isEqualTo(4);
        assertThat(actual.dateOfMeterReading().getMonthDay()).isEqualTo(23);

    }

    @Test
    void findByMonthAndUserId_should_throw_MeterReadingNotFound() {
        org.junit.jupiter.api.Assertions.assertThrows(MeterReadingNotFoundException.class,
                () -> thermalMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(6), 2L));
    }
}
