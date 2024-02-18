package ru.rstdv.monitoringservice.unit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.exception.handler.ServiceExceptionHandler;

import ru.rstdv.monitoringservice.in.controller.ThermalMeterReadingController;
import ru.rstdv.monitoringservice.in.controller.UserRegistrationAuthenticationController;
import ru.rstdv.monitoringservice.service.MeterReadingService;

import ru.rstdv.monitoringservice.service.ThermalMeterReadingServiceImpl;


import java.io.*;
import java.time.Year;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class ThermalMeterReadingServletTest {

    private ObjectMapper objectMapper;

    private final Long ID = 1L;

    private MockMvc mockMvc;
    private MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> thermalMeterReadingService;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        thermalMeterReadingService = Mockito.mock(ThermalMeterReadingServiceImpl.class);
        Mockito.mock(ServiceExceptionHandler.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ThermalMeterReadingController(thermalMeterReadingService)).build();
    }

    @DisplayName("find by month and userId should pass")
    @Test
    void find_by_month_and_userId_should_pass() throws Exception {
        var readDto = getReadThermalMeterReadingDto();
        Mockito.doReturn(readDto).when(thermalMeterReadingService).findByMonthAndUserId(new MonthFilterImpl(1), ID);

        mockMvc.perform(get("/api/v1/thermal-meter-readings/users/{id}", ID).param("monthValue", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(ID));
    }

    @DisplayName("find actual should pass")
    @Test
    void find_actual_should_pass() throws Exception {
        var readDto = getReadThermalMeterReadingDto();
        Mockito.doReturn(readDto).when(thermalMeterReadingService).findActualByUserId(ID);

        mockMvc.perform(get("/api/v1/thermal-meter-readings/actual/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(ID));
    }

    @DisplayName("find actual should pass")
    @Test
    void find_all_by_userId_should_pass() throws Exception {
        var readDto1 = getReadThermalMeterReadingDto();
        var readDto2 = getReadThermalMeterReadingDto();
        var readDto3 = getReadThermalMeterReadingDto();
        Mockito.doReturn(List.of(readDto1, readDto2, readDto3)).when(thermalMeterReadingService).findAllByUserId(ID);

        mockMvc.perform(get("/api/v1/thermal-meter-readings/all/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3));
    }

    @DisplayName("create should pass")
    @Test
    void create_should_pass() throws Exception {
        var createDto = getCreateUpdateThermalMeterReadingDto();
        var json = objectMapper.writeValueAsString(createDto);
        mockMvc.perform(post("/api/v1/thermal-meter-readings")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    private CreateUpdateThermalMeterReadingDto getCreateUpdateThermalMeterReadingDto() {
        return new CreateUpdateThermalMeterReadingDto(
                "1",
                "200");
    }

    private ReadThermalMeterReadingDto getReadThermalMeterReadingDto() {
        return new ReadThermalMeterReadingDto(
                "1",
                "1",
                "200",
                MeterReadingDate.builder()
                        .year(Year.of(2024))
                        .month(1)
                        .monthDay(29)
                        .build()
        );
    }
}
