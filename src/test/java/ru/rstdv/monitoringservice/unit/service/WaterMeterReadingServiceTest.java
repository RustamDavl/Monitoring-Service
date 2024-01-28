package ru.rstdv.monitoringservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class WaterMeterReadingServiceTest {
    @Mock
    private UserRepository userRepositoryImpl;

    @Mock
    private AuditService auditServiceImpl;
    @Mock
    private WaterMeterMapper waterMeterMapperImpl;

    @Mock
    private MeterReadingRepository<WaterMeterReading> waterMeterReadingRepositoryImpl;

    @InjectMocks
    private WaterMeterReadingServiceImpl waterMeterReadingServiceImpl;

    @Test
    void save_should_pass() {
        var thermalReadingDate = LocalDateTime.now();

        var user = User.builder()
                .id(1L)
                .firstname("Vi")
                .email("vivi@gmail.com")
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

        var thermalMeterReading = WaterMeterReading.builder()
                .user(user)
                .dateOfMeterReading(thermalReadingDate)
                .coldWater(123)
                .hotWater(120)
                .build();

        var savedThermalMeterReading = WaterMeterReading.builder()
                .id(1L)
                .user(user)
                .dateOfMeterReading(thermalReadingDate)
                .coldWater(123)
                .hotWater(120)
                .build();

        var createUpdateThermalMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                "1",
                "123",
                "120"
        );
        var readThermalMeterReadingDto = new ReadWaterMeterReadingDto(
                "1",
                new ReadUserDto(
                        "1",
                        "Vi",
                        "vivi@gmail.com",
                        Address.builder()
                                .city("Nigh city")
                                .street("jig-jig")
                                .houseNumber("1")
                                .build(),
                        "USER",
                        "999999999"
                ),
                "123",
                "120",
                thermalReadingDate.toString()
        );

        doReturn(Optional.of(user)).when(userRepositoryImpl).findById(1L);
        doReturn(thermalMeterReading).when(waterMeterMapperImpl).toWaterMeterReading(createUpdateThermalMeterReadingDto, user);
        doReturn(savedThermalMeterReading).when(waterMeterReadingRepositoryImpl).save(thermalMeterReading);
        doReturn(readThermalMeterReadingDto).when(waterMeterMapperImpl).toReadWaterMeterReadingDto(savedThermalMeterReading);

        var actualResult = waterMeterReadingServiceImpl.save(createUpdateThermalMeterReadingDto);

        assertThat(actualResult).isEqualTo(readThermalMeterReadingDto);
    }

    @Test
    void save_should_throw_UserNotFoundException() {
        var user = User.builder()
                .id(1L)
                .firstname("Vi")
                .email("vivi@gmail.com")
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


        var createUpdateThermalMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                "1",
                "123",
                "120"
        );

        doReturn(Optional.empty()).when(userRepositoryImpl).findById(1L);


        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> waterMeterReadingServiceImpl.save(createUpdateThermalMeterReadingDto));

    }

    @Test
    void findActualByUserId_should_pass() {
        /// TODO: 28.01.2024
    }

    @Test
    void findActualByUserId_should_throw_MeterReadingNotFound() {
        /// TODO: 28.01.2024
    }

    @Test
    void findAllByUserId() {
        // TODO: 28.01.2024
    }

    @Test
    void findByMonthAndUserId_should_pass() {

    }

    @Test
    void findByMonthAndUserId_should_throw_MeterReadingNotFound() {

    }
}
