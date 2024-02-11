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
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.factory.ServiceFactoryImpl;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.validator.CreateUpdateUserDtoValidator;
import ru.rstdv.monitoringservice.validator.ValidationResult;
import ru.rstdv.monitoringservice.validator.Validator;

import java.io.IOException;
import java.io.Writer;

import static jakarta.servlet.http.HttpServletResponse.*;
@Loggable
@WebServlet("/registration")
@RequiredArgsConstructor
public class UserRegistrationServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final ServiceFactory serviceFactory;
    private final UserService userService;
    private final Validator<CreateUpdateUserDto> validator;

    public UserRegistrationServlet() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serviceFactory = new ServiceFactoryImpl();
        validator = new CreateUpdateUserDtoValidator();
        userService = serviceFactory.createUserService();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        CreateUpdateUserDto createUpdateUserDto = objectMapper.readValue(req.getReader(), CreateUpdateUserDto.class);
        var validationResult = validator.createValidationResult(createUpdateUserDto);
        if (validationResult.isValid()) {
            tryRegister(createUpdateUserDto, resp);
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

    private void tryRegister(CreateUpdateUserDto createUpdateUserDto, HttpServletResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            var obj = userService.register(createUpdateUserDto);
            response.setStatus(SC_CREATED);
            writer.write(objectMapper.writeValueAsString(obj));
        } catch (EmailRegisteredException e) {
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
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // TODO: 10.02.2024
            }
        }
    }
}
