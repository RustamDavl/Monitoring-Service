package ru.rstdv.monitoringservice.intergration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.util.IntegrationTestBase;
import ru.rstdv.monitoringservice.mapper.*;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.service.*;
import ru.rstdv.monitoringservice.util.LiquibaseUtil;
import ru.rstdv.monitoringservice.util.TestConnectionProvider;

import static org.assertj.core.api.Assertions.assertThat;

public class WaterMeterReadingServiceITFactory extends IntegrationTestBase {

    private UserRepository userRepository;
    private AuditService auditService;
    private WaterMeterMapper waterMeterMapper;

    private UserMapper userMapper;
    private MeterReadingRepository<WaterMeterReading> waterMeterReadingRepository;
    private MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> waterMeterReadingService;

    @BeforeEach
    void setUp() {
        TestConnectionProvider testConnectionProvider = new TestConnectionProvider(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        LiquibaseUtil.start(testConnectionProvider);
        userRepository = new UserRepositoryImpl(testConnectionProvider);
        waterMeterReadingRepository = new WaterMeterReadingRepositoryImpl(testConnectionProvider);
        waterMeterMapper = new WaterMeterMapperImpl();
        userMapper = new UserMapperImpl();
        auditService = new AuditServiceImpl(new AuditRepositoryImpl(testConnectionProvider), new AuditMapperImpl(userMapper), userRepository);
        waterMeterReadingService = new WaterMeterReadingServiceImpl(waterMeterReadingRepository, userRepository, waterMeterMapper, auditService);

    }

    @AfterEach
    void clear() {
        LiquibaseUtil.dropAll();
    }


    @Test
    void save_should_pass() {
        var user = userRepository.findById(2L).get();

        CreateUpdateWaterMeterReadingDto request = new CreateUpdateWaterMeterReadingDto(
                user.getId().toString(),
                "150",
                "80"
        );
        var savedThermalMeterReading = waterMeterReadingService.save(request);

        assertThat(savedThermalMeterReading.userId()).isEqualTo(user.getId().toString());
        assertThat(savedThermalMeterReading.coldWater()).isEqualTo(request.coldWater());
        assertThat(savedThermalMeterReading.hotWater()).isEqualTo(request.hotWater());
    }

    @Test
    void save_should_throw_UserNotFoundException() {
        var createUpdateWaterMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                "30",
                "160",
                "123"
        );
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> waterMeterReadingService.save(createUpdateWaterMeterReadingDto));

    }

    @Test
    void findActualByUserId_should_pass() {
        var actual = waterMeterReadingService.findActualByUserId(2L);
        assertThat(actual.coldWater()).isEqualTo("300");
        assertThat(actual.hotWater()).isEqualTo("240");
        assertThat(actual.dateOfMeterReading().getYear().getValue()).isEqualTo(2023);
        assertThat(actual.dateOfMeterReading().getMonth()).isEqualTo(5);
        assertThat(actual.dateOfMeterReading().getMonthDay()).isEqualTo(20);
    }

    @Test
    void findActualByUserId_should_throw_UserNotFoundException() {
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> waterMeterReadingService.findActualByUserId(30L));
    }

    @Test
    void findAllByUserId() {
        var thermalMeterReadings = waterMeterReadingService.findAllByUserId(2L);
        assertThat(thermalMeterReadings).hasSize(3);
    }

    @Test
    void findByMonthAndUserId_should_pass() {
        var actual = waterMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(3), 2L);
        assertThat(actual.coldWater()).isEqualTo("100");
        assertThat(actual.hotWater()).isEqualTo("80");
        assertThat(actual.dateOfMeterReading().getYear().getValue()).isEqualTo(2023);
        assertThat(actual.dateOfMeterReading().getMonth()).isEqualTo(3);
        assertThat(actual.dateOfMeterReading().getMonthDay()).isEqualTo(20);

    }

    @Test
    void findByMonthAndUserId_should_throw_MeterReadingNotFound() {
        org.junit.jupiter.api.Assertions.assertThrows(MeterReadingNotFoundException.class,
                () -> waterMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(6), 2L));
    }
}
