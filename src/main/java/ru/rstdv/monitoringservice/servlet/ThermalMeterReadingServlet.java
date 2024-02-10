package ru.rstdv.monitoringservice.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.MeterReadingService;
import ru.rstdv.monitoringservice.validator.CreateUpdateThermalMeterReadingDtoValidator;
import ru.rstdv.monitoringservice.validator.ValidationResult;
import ru.rstdv.monitoringservice.validator.Validator;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

@WebServlet("/thermal-meter-readings")
public class ThermalMeterReadingServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    private final ServiceFactory serviceFactory;

    private final MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> thermalMeterReadingService;

    private final Validator<CreateUpdateThermalMeterReadingDto> validator;

    public ThermalMeterReadingServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serviceFactory = new ServiceFactoryImpl();
        thermalMeterReadingService = serviceFactory.createThermalMeterReadingService();
        validator = new CreateUpdateThermalMeterReadingDtoValidator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var userId = req.getParameter("userId");
        var monthValue = req.getParameter("monthValue");
        resp.setContentType("application/json");
        tryFindByMonthAndUserId(Long.valueOf(userId), Integer.parseInt(monthValue), resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto = objectMapper.readValue(req.getInputStream(), CreateUpdateThermalMeterReadingDto.class);
        resp.setContentType("application/json");
        var validationResult = validator.createValidationResult(createUpdateThermalMeterReadingDto);
        if (validationResult.isValid()) {
            trySave(createUpdateThermalMeterReadingDto, resp);
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

    private void trySave(CreateUpdateThermalMeterReadingDto object, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var obj = thermalMeterReadingService.save(object);
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
            var obj = thermalMeterReadingService.findByMonthAndUserId(new MonthFilterImpl(monthValue), id);
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