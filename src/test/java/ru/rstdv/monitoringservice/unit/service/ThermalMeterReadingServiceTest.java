package ru.rstdv.monitoringservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.User;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.mapper.ThermalMeterMapper;
import ru.rstdv.monitoringservice.repository.MeterReadingRepository;
import ru.rstdv.monitoringservice.repository.UserRepository;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.ThermalMeterReadingServiceImpl;

import java.time.Year;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ThermalMeterReadingServiceTest {
    @Mock
    private UserRepository userRepositoryImpl;
    @Mock
    private AuditService auditServiceImpl;
    @Mock
    private ThermalMeterMapper thermalMeterMapperImpl;
    @Mock
    private MeterReadingRepository<ThermalMeterReading> thermalMeterReadingRepositoryImpl;
    @InjectMocks
    private ThermalMeterReadingServiceImpl thermalMeterReadingServiceImpl;
    private final Long ID = 1L;

    @Test
    void save_should_pass() {
        var user = getUser();
        var thermalMeterReading = getThermalMeterReading();
        var savedThermalMeterReading = getSavedThermalMeterReading();
        var createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(
                "1",
                "123.4"
        );
        var readThermalMeterReadingDto = getReadThermalMeterReadingDto();

        doReturn(Optional.of(user)).when(userRepositoryImpl).findById(1L);
        doReturn(thermalMeterReading).when(thermalMeterMapperImpl).toThermalMeterReading(createUpdateThermalMeterReadingDto, user);
        doReturn(savedThermalMeterReading).when(thermalMeterReadingRepositoryImpl).save(thermalMeterReading);
        doReturn(readThermalMeterReadingDto).when(thermalMeterMapperImpl).toReadThermalMeterReadingDto(savedThermalMeterReading);

        var actualResult = thermalMeterReadingServiceImpl.save(createUpdateThermalMeterReadingDto);

        assertThat(actualResult).isEqualTo(readThermalMeterReadingDto);
    }

    @Test
    void save_should_throw_UserNotFoundException() {
        var createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(
                "1",
                "123.4"
        );
        doReturn(Optional.empty()).when(userRepositoryImpl).findById(ID);
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> thermalMeterReadingServiceImpl.save(createUpdateThermalMeterReadingDto));

    }

    @Test
    void findActualByUserId_should_pass() {
        var readDto = getReadThermalMeterReadingDto();
        var thermalMeterReading = getThermalMeterReading();
        doReturn(Optional.of(thermalMeterReading)).when(thermalMeterReadingRepositoryImpl).findActualByUserId(ID);
        doReturn(readDto).when(thermalMeterMapperImpl).toReadThermalMeterReadingDto(thermalMeterReading);

        var actual = thermalMeterReadingServiceImpl.findActualByUserId(ID);

        assertThat(actual).isEqualTo(getReadThermalMeterReadingDto());

    }

    @Test
    void findActualByUserId_should_throw_MeterReadingNotFound() {
        doReturn(Optional.empty()).when(thermalMeterReadingRepositoryImpl).findActualByUserId(ID);
        org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> thermalMeterReadingServiceImpl.findActualByUserId(ID));

    }

    @Test
    void findByMonthAndUserId_should_pass() {
        var readDto = getReadThermalMeterReadingDto();
        var thermalReading = getThermalMeterReading();
        doReturn(Optional.of(thermalReading)).when(thermalMeterReadingRepositoryImpl).findByMonthAndUserId(new MonthFilterImpl(1), ID);
        doReturn(readDto).when(thermalMeterMapperImpl).toReadThermalMeterReadingDto(thermalReading);
        var actualResult = thermalMeterReadingServiceImpl.findByMonthAndUserId(new MonthFilterImpl(1), ID);
        assertThat(actualResult).isEqualTo(getReadThermalMeterReadingDto());

    }

    @Test
    void findByMonthAndUserId_should_throw_MeterReadingNotFound() {
        doReturn(Optional.empty()).when(thermalMeterReadingRepositoryImpl).findByMonthAndUserId(new MonthFilterImpl(1), ID);
        org.junit.jupiter.api.Assertions.assertThrows(MeterReadingNotFoundException.class,
                () -> thermalMeterReadingServiceImpl.findByMonthAndUserId(new MonthFilterImpl(1), ID));

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

    private ThermalMeterReading getThermalMeterReading() {
        return ThermalMeterReading.builder()
                .userId(1L)
                .meterReadingDate(getMeterReadingDateConstant())
                .gigaCalories(123.4F)
                .build();
    }

    private ThermalMeterReading getSavedThermalMeterReading() {
        return ThermalMeterReading.builder()
                .id(ID)
                .userId(1L)
                .meterReadingDate(getMeterReadingDateConstant())
                .gigaCalories(123.4F)
                .build();
    }

    private MeterReadingDate getMeterReadingDateConstant() {
        return MeterReadingDate.builder()
                .year(Year.now())
                .month(1)
                .monthDay(23)
                .build();
    }

    private ReadThermalMeterReadingDto getReadThermalMeterReadingDto() {
        return new ReadThermalMeterReadingDto(
                ID.toString(),
                "1",
                "123.4",
                getMeterReadingDateConstant()
        );
    }
}
