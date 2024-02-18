package ru.rstdv.monitoringservice.unit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.exception.handler.ServiceExceptionHandler;
import ru.rstdv.monitoringservice.in.controller.ThermalMeterReadingController;
import ru.rstdv.monitoringservice.in.controller.WaterMeterReadingController;
import ru.rstdv.monitoringservice.service.MeterReadingService;
import ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl;

import java.time.Year;
import java.util.List;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class WaterMeterReadingServletTest {
    private ObjectMapper objectMapper;

    private final Long ID = 1L;

    private MockMvc mockMvc;
    private MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> waterMeterReadingsService;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        waterMeterReadingsService = Mockito.mock(WaterMeterReadingServiceImpl.class);
        Mockito.mock(ServiceExceptionHandler.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new WaterMeterReadingController(waterMeterReadingsService)).build();
    }

    @DisplayName("find by month and userId should pass")
    @Test
    void find_by_month_and_userId_should_pass() throws Exception {
        var readDto = getReadWaterMeterReadingDto();
        Mockito.doReturn(readDto).when(waterMeterReadingsService).findByMonthAndUserId(new MonthFilterImpl(1), ID);

        mockMvc.perform(get("/api/v1/water-meter-readings/users/{id}", ID).param("monthValue", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(ID));
    }

    @DisplayName("find actual should pass")
    @Test
    void find_actual_should_pass() throws Exception {
        var readDto = getReadWaterMeterReadingDto();
        Mockito.doReturn(readDto).when(waterMeterReadingsService).findActualByUserId(ID);

        mockMvc.perform(get("/api/v1/water-meter-readings/actual/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(ID));
    }

    @DisplayName("find actual should pass")
    @Test
    void find_all_by_userId_should_pass() throws Exception {
        var readDto1 = getReadWaterMeterReadingDto();
        var readDto2 = getReadWaterMeterReadingDto();
        var readDto3 = getReadWaterMeterReadingDto();
        Mockito.doReturn(List.of(readDto1, readDto2, readDto3)).when(waterMeterReadingsService).findAllByUserId(ID);

        mockMvc.perform(get("/api/v1/water-meter-readings/all/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3));
    }

    @DisplayName("create should pass")
    @Test
    void create_should_pass() throws Exception {
        var createDto = getCreateUpdateWaterMeterReadingDto();
        var json = objectMapper.writeValueAsString(createDto);
        mockMvc.perform(post("/api/v1/water-meter-readings")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    private CreateUpdateWaterMeterReadingDto getCreateUpdateWaterMeterReadingDto() {
        return new CreateUpdateWaterMeterReadingDto(
                "1",
                "200",
                "100"
        );
    }

    private ReadWaterMeterReadingDto getReadWaterMeterReadingDto() {
        return new ReadWaterMeterReadingDto(
                "1",
                "1",
                "200",
                "100",
                MeterReadingDate.builder()
                        .year(Year.of(2024))
                        .month(1)
                        .monthDay(29)
                        .build()
        );
    }
}
