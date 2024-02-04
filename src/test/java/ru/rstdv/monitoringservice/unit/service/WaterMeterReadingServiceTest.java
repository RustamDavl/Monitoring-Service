package ru.rstdv.monitoringservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.WaterMeterMapper;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl;

import java.time.Year;
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

    private final Long ID = 1L;

    @Test
    void save_should_pass() {
        var user = getUser();
        var waterMeterReading = getWaterMeterReading();
        var savedWaterMeterReading = getSavedWaterMeterReading();
        var createUpdateWaterMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                "1",
                "150",
                "100"
        );
        var readThermalMeterReadingDto = getReadWaterMeterReadingDto();

        doReturn(Optional.of(user)).when(userRepositoryImpl).findById(1L);
        doReturn(waterMeterReading).when(waterMeterMapperImpl).toWaterMeterReading(createUpdateWaterMeterReadingDto, user);
        doReturn(savedWaterMeterReading).when(waterMeterReadingRepositoryImpl).save(waterMeterReading);
        doReturn(readThermalMeterReadingDto).when(waterMeterMapperImpl).toReadWaterMeterReadingDto(savedWaterMeterReading);

        var actualResult = waterMeterReadingServiceImpl.save(createUpdateWaterMeterReadingDto);

        assertThat(actualResult).isEqualTo(readThermalMeterReadingDto);
    }
    @Test
    void save_should_throw_UserNotFoundException() {
        var createUpdateWaterMeterReadingDto = new CreateUpdateWaterMeterReadingDto(
                "1",
                "150",
                "100"
        );
        doReturn(Optional.empty()).when(userRepositoryImpl).findById(ID);
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> waterMeterReadingServiceImpl.save(createUpdateWaterMeterReadingDto));
    }
    @Test
    void findActualByUserId_should_pass() {
        var readDto = getReadWaterMeterReadingDto();
        var waterMeterReading = getWaterMeterReading();
        doReturn(Optional.of(waterMeterReading)).when(waterMeterReadingRepositoryImpl).findActualByUserId(ID);
        doReturn(readDto).when(waterMeterMapperImpl).toReadWaterMeterReadingDto(waterMeterReading);

        var actual = waterMeterReadingServiceImpl.findActualByUserId(ID);

        assertThat(actual).isEqualTo(getReadWaterMeterReadingDto());

    }

    @Test
    void findActualByUserId_should_throw_MeterReadingNotFound() {
        doReturn(Optional.empty()).when(waterMeterReadingRepositoryImpl).findActualByUserId(ID);
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> waterMeterReadingServiceImpl.findActualByUserId(ID));

    }

    @Test
    void findByMonthAndUserId_should_pass() {
        var readDto = getReadWaterMeterReadingDto();
        var waterReading = getWaterMeterReading();
        doReturn(Optional.of(waterReading)).when(waterMeterReadingRepositoryImpl).findByMonthAndUserId(new MonthFilterImpl(1), ID);
        doReturn(readDto).when(waterMeterMapperImpl).toReadWaterMeterReadingDto(waterReading);
        var actualResult = waterMeterReadingServiceImpl.findByMonthAndUserId(new MonthFilterImpl(1), ID);
        assertThat(actualResult).isEqualTo(getReadWaterMeterReadingDto());

    }

    @Test
    void findByMonthAndUserId_should_throw_MeterReadingNotFound() {
        doReturn(Optional.empty()).when(waterMeterReadingRepositoryImpl).findByMonthAndUserId(new MonthFilterImpl(1), ID);
        org.junit.jupiter.api.Assertions.assertThrows(MeterReadingNotFoundException.class,
                () -> waterMeterReadingServiceImpl.findByMonthAndUserId(new MonthFilterImpl(1), ID));

    }

    private User getUser() {
        return User.builder()
                .id(ID)
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
    }

    private WaterMeterReading getWaterMeterReading() {
        return WaterMeterReading.builder()
                .userId(1L)
                .meterReadingDate(getMeterReadingDateConstant())
                .coldWater(150)
                .hotWater(100)
                .build();
    }

    private WaterMeterReading getSavedWaterMeterReading() {
        return WaterMeterReading.builder()
                .id(ID)
                .userId(1L)
                .meterReadingDate(getMeterReadingDateConstant())
                .coldWater(150)
                .hotWater(100)
                .build();
    }

    private MeterReadingDate getMeterReadingDateConstant() {
        return MeterReadingDate.builder()
                .year(Year.now())
                .month(1)
                .monthDay(23)
                .build();
    }

    private ReadWaterMeterReadingDto getReadWaterMeterReadingDto() {
        return new ReadWaterMeterReadingDto(
                ID.toString(),
                "1",
                "150",
                "100",
                getMeterReadingDateConstant()
        );
    }
}
