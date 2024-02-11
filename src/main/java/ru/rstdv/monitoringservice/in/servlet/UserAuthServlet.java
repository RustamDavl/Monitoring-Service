package ru.rstdv.monitoringservice.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.UserAuthDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.validator.UserAuthDtoValidator;
import ru.rstdv.monitoringservice.validator.ValidationResult;
import ru.rstdv.monitoringservice.validator.Validator;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

/**
 * сервлет ответственнен за аутентификацию пользователя
 * POST : /monitoring-service/authentication
 */
@Loggable
@WebServlet("/authentication")
@RequiredArgsConstructor
public class UserAuthServlet extends HttpServlet {

    private final ObjectMapper objectMapper;

    private final ServiceFactory serviceFactory;

    private final UserService userService;

    private final Validator<UserAuthDto> validator;

    private static final String USER_ATTRIBUTE = "user";

    public UserAuthServlet() {
        objectMapper = new ObjectMapper();
        serviceFactory = new ServiceFactoryImpl();
        validator = new UserAuthDtoValidator();
        userService = serviceFactory.createUserService();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        var userAuthDto = objectMapper.readValue(req.getReader(), UserAuthDto.class);
        var validationResult = validator.createValidationResult(userAuthDto);

        if (validationResult.isValid()) {
            var session = req.getSession();
            var maybeUser = (ReadUserDto) session.getAttribute(USER_ATTRIBUTE);
            try (var writer = resp.getWriter()) {
                if (maybeUser == null) {
                    var authenticatedUser = tryAuthenticate(userAuthDto, resp);
                    if (authenticatedUser != null) {
                        session.setAttribute(USER_ATTRIBUTE, authenticatedUser);
                        resp.setStatus(SC_OK);
                        writer.write(objectMapper.writeValueAsString(authenticatedUser));
                        writer.flush();
                    }
                } else {
                    resp.setStatus(SC_OK);
                    writer.write("already authenticated");
                    writer.flush();
                }
            }
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

    private ReadUserDto tryAuthenticate(UserAuthDto userAuthDto, HttpServletResponse resp) {
        try {
            return userService.authenticate(userAuthDto.email(), userAuthDto.password());
        } catch (UserNotFoundException e) {
            try (var writer = resp.getWriter()) {
                resp.setStatus(SC_BAD_REQUEST);
                writer.write(e.getMessage());
                writer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
}
