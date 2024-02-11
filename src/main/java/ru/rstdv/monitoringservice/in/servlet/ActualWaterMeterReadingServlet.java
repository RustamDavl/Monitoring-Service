package ru.rstdv.monitoringservice.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.MeterReadingService;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

/**
 * сервлет ответственнен за отображение последнего показания счетчика воды пользователя
 * GET : /monitoring-service/actual-water-meter-readings/all?userId=id
 */
@Loggable
@WebServlet("/actual-water-meter-reading")
public class ActualWaterMeterReadingServlet extends HttpServlet {
    private final ObjectMapper objectMapper;

    private final ServiceFactory serviceFactory;

    private final MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> waterMeterReadingService;

    public ActualWaterMeterReadingServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serviceFactory = new ServiceFactoryImpl();
        waterMeterReadingService = serviceFactory.createWaterMeterReadingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var userId = req.getParameter("userId");
        resp.setContentType("application/json");
        tryFindActualByUserId(Long.valueOf(userId), resp);
    }

    private void tryFindActualByUserId(Long id, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var obj = waterMeterReadingService.findActualByUserId(id);
            response.setStatus(HttpServletResponse.SC_OK);
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
}
