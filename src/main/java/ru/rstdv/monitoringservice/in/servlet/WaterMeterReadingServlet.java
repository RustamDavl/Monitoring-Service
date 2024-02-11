package ru.rstdv.monitoringservice.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.MeterReadingService;
import ru.rstdv.monitoringservice.validator.CreateUpdateWaterMeterReadingDtoValidator;
import ru.rstdv.monitoringservice.validator.ValidationResult;
import ru.rstdv.monitoringservice.validator.Validator;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

/**
 * сервлет ответственнен за подачу показаний счетчика воды и за отображение показаний пользователя за конкретный месяц
 * POST : /monitoring-service/water-meter-readings
 * GET : /monitoring-service/water-meter-readings?userId=id&monthValue=value
 */
@Loggable
@WebServlet("/water-meter-readings")
@RequiredArgsConstructor
public class WaterMeterReadingServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    private final ServiceFactory serviceFactory;

    private final MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> waterMeterReadingService;
    private final Validator<CreateUpdateWaterMeterReadingDto> validator;

    public WaterMeterReadingServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serviceFactory = new ServiceFactoryImpl();
        waterMeterReadingService = serviceFactory.createWaterMeterReadingService();
        validator = new CreateUpdateWaterMeterReadingDtoValidator();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var userId = req.getParameter("userId");
        var monthValue = req.getParameter("monthValue");
        resp.setContentType("application/json");
        tryFindByMonthAndUserId(Long.valueOf(userId), Integer.parseInt(monthValue), resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto = objectMapper.readValue(req.getReader(), CreateUpdateWaterMeterReadingDto.class);
        resp.setContentType("application/json");
        var validationResult = validator.createValidationResult(createUpdateWaterMeterReadingDto);
        if (validationResult.isValid()) {
            trySave(createUpdateWaterMeterReadingDto, resp);
        } else {
            sendErrors(validationResult, resp);
        }

    }

    private void sendErrors(ValidationResult validationResult, HttpServletResponse response) {
        try (Writer writer = response.getWriter()) {
            validationResult.getErrors()
                    .forEach(
                            error -> {
                                try {
                                    writer.write(error);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
            response.setStatus(SC_BAD_REQUEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void trySave(CreateUpdateWaterMeterReadingDto object, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var obj = waterMeterReadingService.save(object);
            response.setStatus(HttpServletResponse.SC_CREATED);
            writer.write(objectMapper.writeValueAsString(obj));
        } catch (UserNotFoundException e) {
            try {
                response.setStatus(SC_CONFLICT);
                writer = response.getWriter();
                writer.write(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            // TODO: 09.02.2024 logging
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                // TODO: 10.02.2024
            }
        }
    }

    private void tryFindByMonthAndUserId(Long id, Integer monthValue, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var obj = waterMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(monthValue), id);
            response.setStatus(HttpServletResponse.SC_OK);
            writer.write(objectMapper.writeValueAsString(obj));
        } catch (IncorrectMonthValueException | MeterReadingNotFoundException e) {
            try {
                response.setStatus(SC_CONFLICT);
                writer = response.getWriter();
                writer.write(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            // TODO: 09.02.2024 logging
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                // TODO: 10.02.2024
            }
        }
    }
}
