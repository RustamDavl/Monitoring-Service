package ru.rstdv.monitoringservice.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.MeterReadingService;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;


@WebServlet("/thermal-meter-readings/all")
public class FindAllThermalMeterReadingServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    private final ServiceFactory serviceFactory;

    private final MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> thermalMeterReadingService;

    public FindAllThermalMeterReadingServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serviceFactory = new ServiceFactoryImpl();
        thermalMeterReadingService = serviceFactory.createThermalMeterReadingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        var userId = req.getParameter("userId");
        tryFindAllByUserId(Long.valueOf(userId), resp);

    }

    private void tryFindAllByUserId(Long id, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var list = thermalMeterReadingService.findAllByUserId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            Writer finalWriter = writer;
            list.forEach(
                    readWaterMeterReadingDto -> {
                        try {
                            finalWriter.write(objectMapper.writeValueAsString(readWaterMeterReadingDto));
                        } catch (IOException e) {
                            // TODO: 10.02.2024 logging
                        }
                    }
            );
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
