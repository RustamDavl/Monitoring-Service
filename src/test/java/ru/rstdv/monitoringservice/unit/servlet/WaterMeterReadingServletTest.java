package ru.rstdv.monitoringservice.unit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.service.MeterReadingService;
import ru.rstdv.monitoringservice.servlet.WaterMeterReadingServlet;
import ru.rstdv.monitoringservice.validator.ValidationResult;
import ru.rstdv.monitoringservice.validator.Validator;

import java.io.*;
import java.time.Year;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WaterMeterReadingServletTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private ValidationResult validationResult;
    @Mock
    private MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> waterMeterReadingService;
    @Mock
    private Validator<CreateUpdateWaterMeterReadingDto> validator;

    @InjectMocks
    WaterMeterReadingServlet servlet;


    @DisplayName("doGet should save water meter readings successfully")
    @Test
    void doGet_should_find_by_month_and_userId_successfully() throws IOException {
        String jsonResponse = "{\"id\": \"1\"," +
                              "\"userId\": \"1\"," +
                              " \"coldWater\": \"200\", " +
                              "\"hotWater\":\"100\"" +
                              "\"dateOfMeterReading\": {\n" +
                              "        \"year\": \"2024\",\n" +
                              "        \"month\": \"1\",\n" +
                              "        \"monthDay\": \"29\"\n" +
                              "    }" +
                              "}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        ReadWaterMeterReadingDto readWaterMeterReadingDto = getReadWaterMeterReadingDto();
        var reader = new BufferedReader(new StringReader(jsonResponse));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String userId = "1";
        String monthValue = "1";
        doReturn(userId).when(request).getParameter("userId");
        doReturn(monthValue).when(request).getParameter("monthValue");
        doReturn(writer).when(response).getWriter();
        doReturn(readWaterMeterReadingDto).when(waterMeterReadingService).findByMonthAndUserId(new MonthFilterImpl(Integer.parseInt(monthValue)), Long.valueOf(userId));
        doReturn(jsonResponse).when(objectMapper).writeValueAsString(readWaterMeterReadingDto);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @DisplayName("doGet should throw IncorrectMonthValueException")
    @Test
    void doGet_should_throw_IncorrectMonthValueException() throws IOException {

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String userId = "1";
        String monthValue = "99";
        doReturn(userId).when(request).getParameter("userId");
        doReturn(monthValue).when(request).getParameter("monthValue");
        doReturn(writer).when(response).getWriter();
        doThrow(new IncorrectMonthValueException("incorrect month value")).when(waterMeterReadingService).findByMonthAndUserId(new MonthFilterImpl(Integer.parseInt(monthValue)), Long.valueOf(userId));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @DisplayName("doGet should throw MeterReadingNotFoundException")
    @Test
    void doGet_should_throw_MeterReadingNotFoundException() throws IOException {

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String userId = "1";
        String monthValue = "99";
        doReturn(userId).when(request).getParameter("userId");
        doReturn(monthValue).when(request).getParameter("monthValue");
        doReturn(writer).when(response).getWriter();
        doThrow(new MeterReadingNotFoundException("no entity by these month value and userId")).when(waterMeterReadingService).findByMonthAndUserId(new MonthFilterImpl(Integer.parseInt(monthValue)), Long.valueOf(userId));

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @DisplayName("doPost should save water meter readings successfully")
    @Test
    void doPost_should_save_water_meter_readings_successfully() throws IOException {
        String jsonRequest = "{\"userId\": \"1\"," +
                             " \"coldWater\": \"200\", " +
                             "\"hotWater\":\"100\"}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto = getCreateUpdateWaterMeterReadingDto();
        ReadWaterMeterReadingDto readWaterMeterReadingDto = getReadWaterMeterReadingDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(createUpdateWaterMeterReadingDto).when(objectMapper).readValue(reader, CreateUpdateWaterMeterReadingDto.class);
        doReturn(validationResult).when(validator).createValidationResult(createUpdateWaterMeterReadingDto);
        doReturn(true).when(validationResult).isValid();
        doReturn(readWaterMeterReadingDto).when(waterMeterReadingService).save(createUpdateWaterMeterReadingDto);
        doReturn(jsonRequest).when(objectMapper).writeValueAsString(readWaterMeterReadingDto);

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @DisplayName("doPost save water meter readings should sent errors because of incorrect request data")
    @Test
    void doPost_save_water_meter_readings_should_sent_errors_because_of_incorrect_request_data() throws IOException {
        String jsonRequest = "{\"userId\": \"1\"," +
                             " \"coldWater\": \"200\", " +
                             "\"hotWater\":\"100\"}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto = getCreateUpdateWaterMeterReadingDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(createUpdateWaterMeterReadingDto).when(objectMapper).readValue(reader, CreateUpdateWaterMeterReadingDto.class);
        doReturn(validationResult).when(validator).createValidationResult(createUpdateWaterMeterReadingDto);
        doReturn(false).when(validationResult).isValid();

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @DisplayName("doPost save water meter readings should throw UserNotFoundException")
    @Test
    void doPost_save_water_meter_readings_should_throw_UserNotFoundException() throws IOException {
        String jsonRequest = "{\"userId\": \"1\"," +
                             " \"coldWater\": \"200\", " +
                             "\"hotWater\":\"100\"}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto = getCreateUpdateWaterMeterReadingDto();
        ReadWaterMeterReadingDto readWaterMeterReadingDto = getReadWaterMeterReadingDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(createUpdateWaterMeterReadingDto).when(objectMapper).readValue(reader, CreateUpdateWaterMeterReadingDto.class);
        doReturn(validationResult).when(validator).createValidationResult(createUpdateWaterMeterReadingDto);
        doReturn(true).when(validationResult).isValid();
        doThrow(new UserNotFoundException("there is no user")).when(waterMeterReadingService).save(createUpdateWaterMeterReadingDto);


        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
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
