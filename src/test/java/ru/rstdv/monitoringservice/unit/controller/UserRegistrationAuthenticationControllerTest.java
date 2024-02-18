package ru.rstdv.monitoringservice.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.createupdate.UserAuthDto;
import ru.rstdv.monitoringservice.exception.handler.ServiceExceptionHandler;
import ru.rstdv.monitoringservice.in.controller.UserRegistrationAuthenticationController;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.service.UserServiceImpl;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserRegistrationAuthenticationControllerTest {
    private MockMvc mockMvc;
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userService = Mockito.mock(UserServiceImpl.class);
        Mockito.mock(ServiceExceptionHandler.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserRegistrationAuthenticationController(userService)).build();
    }

    @DisplayName("registration should pass")
    @Test
    void registration_should_pass() throws Exception {
        var createUpdateUserDto = createUpdateUserDto();
        var json = objectMapper.writeValueAsString(createUpdateUserDto);
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @DisplayName("authentication should pass")
    @Test
    void authentication_should_pass() throws Exception {
        var userAuthDto = userAuthDto();
        var json = objectMapper.writeValueAsString(userAuthDto);
        mockMvc.perform(post("/api/v1/users/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }


    private CreateUpdateUserDto createUpdateUserDto() {
        return new CreateUpdateUserDto(
                "Rus",
                "test@gmail.com",
                "pass",
                "000000000",
                "City",
                "Street",
                "1"
        );
    }

    private UserAuthDto userAuthDto() {
        return new UserAuthDto("test@gmail.com", "pass");
    }
}
