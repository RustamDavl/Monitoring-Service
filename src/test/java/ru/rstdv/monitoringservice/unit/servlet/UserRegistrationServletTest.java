package ru.rstdv.monitoringservice.unit.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.exception.EmailRegisteredException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.servlet.UserRegistrationServlet;
import ru.rstdv.monitoringservice.validator.CreateUpdateUserDtoValidator;
import ru.rstdv.monitoringservice.validator.ValidationResult;

import java.io.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServletTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ServiceFactory serviceFactory;
    @Mock
    private UserService userService;
    @Mock
    private CreateUpdateUserDtoValidator validator;
    @Mock
    private ValidationResult validationResult;
    @InjectMocks
    UserRegistrationServlet userRegistrationServlet;

    @DisplayName("doPost should register successfully with validation result is valid")
    @Test
    void doPost_should_register_successfully_with_validationResult_isValid() throws IOException {
        String jsonRequest = "{\"firstname\": \"Geralt\"," +
                             " \"email\": \"some@gmail.com\", " +
                             "\"password\":\"password777\"," +
                             "\"personalAccount\":\"000000000\", " +
                             "\"city\":\"Novigrad\" ," +
                             "\"street\":\"some\"," +
                             " \"houseNumber\":\"1\"}";

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        CreateUpdateUserDto createUpdateUserDto = getCreateUpdateUserDto();
        ReadUserDto readUserDto = getReadUserDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(createUpdateUserDto).when(objectMapper).readValue(reader, CreateUpdateUserDto.class);
        doReturn(validationResult).when(validator).createValidationResult(createUpdateUserDto);
        doReturn(true).when(validationResult).isValid();
        doReturn(readUserDto).when(userService).register(createUpdateUserDto);
        doReturn(jsonRequest).when(objectMapper).writeValueAsString(readUserDto);

        userRegistrationServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @DisplayName("doPost should throw EmailRegisteredException")
    @Test
    void doPost_should_throw_EmailRegisteredException() throws IOException, ServletException {
        String jsonRequest = "{\"firstname\": \"Geralt\"," +
                             " \"email\": \"some@gmail.com\", " +
                             "\"password\":\"password777\"," +
                             "\"personalAccount\":\"000000000\", " +
                             "\"city\":\"Novigrad\" ," +
                             "\"street\":\"some\"," +
                             " \"houseNumber\":\"1\"}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        CreateUpdateUserDto createUpdateUserDto = getCreateUpdateUserDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(createUpdateUserDto).when(objectMapper).readValue(reader, CreateUpdateUserDto.class);
        doReturn(validationResult).when(validator).createValidationResult(createUpdateUserDto);
        doReturn(true).when(validationResult).isValid();
        doThrow(new EmailRegisteredException("email already registered")).when(userService).register(createUpdateUserDto);

        userRegistrationServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @DisplayName("doPost should send error because validation result is not valid")
    @Test
    void doPost_should_register_send_error_because_validationResult_is_not_valid() throws IOException, ServletException {
        String jsonRequest = "{\"firstname\": \"Geralt\"," +
                             " \"email\": \"somegmail.com\", " +
                             "\"password\":\"password777\"," +
                             "\"personalAccount\":\"000000000\", " +
                             "\"city\":\"Novigrad\" ," +
                             "\"street\":\"some\"," +
                             " \"houseNumber\":\"1\"}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        var reader = new BufferedReader(new StringReader(jsonRequest));

        CreateUpdateUserDto createUpdateUserDto = getCreateUpdateUserDto();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(createUpdateUserDto).when(objectMapper).readValue(reader, CreateUpdateUserDto.class);
        doReturn(validationResult).when(validator).createValidationResult(createUpdateUserDto);
        doReturn(false).when(validationResult).isValid();

        userRegistrationServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    private CreateUpdateUserDto getCreateUpdateUserDto() {
        return new CreateUpdateUserDto(
                "Geralt",
                "some@gmail.com",
                "password777",
                "000000000",
                "Novigrad",
                "some",
                "1"
        );
    }

    private ReadUserDto getReadUserDto() {
        return new ReadUserDto(
                "1",
                "Geralt",
                "some@gmail.com",
                Address.builder()
                        .city("Novigrad")
                        .street("some")
                        .houseNumber("1")
                        .build(),
                "USER",
                "000000000"
        );
    }
}
