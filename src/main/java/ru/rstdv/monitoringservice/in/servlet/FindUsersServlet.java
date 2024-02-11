package ru.rstdv.monitoringservice.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.UserService;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * сервлет ответственнен за отображение пользователя по идентификатору
 * GET : /monitoring-service/users/{id}; id - идентификатор пользователя
 */
@Loggable
@WebServlet("/users/*")
public class FindUsersServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    private final ServiceFactory serviceFactory;

    private final UserService userService;

    public FindUsersServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serviceFactory = new ServiceFactoryImpl();
        userService = serviceFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var userId = req.getRequestURI().replaceAll("\\D+", "");
        resp.setContentType("application/json");
        if (!userId.isEmpty()) {
            tryFindUserById(Long.valueOf(userId), resp);
        } else {
            findAllUsers(resp);
        }
    }

    private void tryFindUserById(Long id, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var obj = userService.findById(id);
            writer.write(objectMapper.writeValueAsString(obj));
            response.setStatus(SC_OK);
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

    private void findAllUsers(HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var list = userService.findAll();
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
            response.setStatus(SC_OK);
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
