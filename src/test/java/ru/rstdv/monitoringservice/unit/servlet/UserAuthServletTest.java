package ru.rstdv.monitoringservice.unit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.createupdate.UserAuthDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.ServiceFactory;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.servlet.UserAuthServlet;
import ru.rstdv.monitoringservice.validator.UserAuthDtoValidator;
import ru.rstdv.monitoringservice.validator.ValidationResult;

import java.io.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserAuthServletTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ServiceFactory serviceFactory;
    @Mock
    private UserService userService;
    @Mock
    private UserAuthDtoValidator validator;
    @Mock
    private ValidationResult validationResult;
    @InjectMocks
    UserAuthServlet userAuthServlet;

    @DisplayName("try to authenticate user who already authenticated")
    @Test
    void doPost_authenticate_user_who_already_authenticated() throws IOException, ServletException {
        String jsonRequest = "{\"email\": \"some@gmail.com\", " +
                             "\"password\":\"password777\"}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UserAuthDto userAuthDto = getUserAuthDto();
        ReadUserDto readUserDto = getReadUserDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(userAuthDto).when(objectMapper).readValue(reader, UserAuthDto.class);
        doReturn(validationResult).when(validator).createValidationResult(userAuthDto);
        doReturn(true).when(validationResult).isValid();
        doReturn(session).when(request).getSession();
        doReturn(readUserDto).when(session).getAttribute("user");

        userAuthServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }


    @DisplayName("try to authenticate user who is not authenticated")
    @Test
    void doPost_authenticate_user_who_is_not_authenticated() throws IOException, ServletException {
        String jsonRequest = "{\"email\": \"some@gmail.com\", " +
                             "\"password\":\"password777\"}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UserAuthDto userAuthDto = getUserAuthDto();
        ReadUserDto readUserDto = getReadUserDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(userAuthDto).when(objectMapper).readValue(reader, UserAuthDto.class);
        doReturn(validationResult).when(validator).createValidationResult(userAuthDto);
        doReturn(true).when(validationResult).isValid();
        doReturn(session).when(request).getSession();
        doReturn(null).when(session).getAttribute("user");
        doReturn(readUserDto).when(userService).authenticate(userAuthDto.email(), userAuthDto.password());
        doReturn(jsonRequest).when(objectMapper).writeValueAsString(readUserDto);
        userAuthServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(session).setAttribute("user", readUserDto);
    }

    @DisplayName("should throw UserNotFoundException")
    @Test
    void doPost_should_throw_UserNotFoundException() throws IOException, ServletException {
        String jsonRequest = "{\"email\": \"some@gmail.com\", " +
                             "\"password\":\"password777\"}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UserAuthDto userAuthDto = getUserAuthDto();
        ReadUserDto readUserDto = getReadUserDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(userAuthDto).when(objectMapper).readValue(reader, UserAuthDto.class);
        doReturn(validationResult).when(validator).createValidationResult(userAuthDto);
        doReturn(true).when(validationResult).isValid();
        doReturn(session).when(request).getSession();
        doReturn(null).when(session).getAttribute("user");
        doThrow(new UserNotFoundException("bad credentials")).when(userService).authenticate(userAuthDto.email(), userAuthDto.password());
        userAuthServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @DisplayName("should send errors if request parameters are not valid")
    @Test
    void doPost_should_send_errors_if_request_parameters_are_not_valid() throws IOException, ServletException {
        String jsonRequest = "{\"email\": \"some@gmail.com\", " +
                             "\"password\":\"password777\"}";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        UserAuthDto userAuthDto = getUserAuthDto();
        ReadUserDto readUserDto = getReadUserDto();
        var reader = new BufferedReader(new StringReader(jsonRequest));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        doReturn(reader).when(request).getReader();
        doReturn(writer).when(response).getWriter();
        doReturn(userAuthDto).when(objectMapper).readValue(reader, UserAuthDto.class);
        doReturn(validationResult).when(validator).createValidationResult(userAuthDto);
        doReturn(false).when(validationResult).isValid();

        userAuthServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private UserAuthDto getUserAuthDto() {
        return new UserAuthDto(
                "some@gmail.com",
                "password777"
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
}
