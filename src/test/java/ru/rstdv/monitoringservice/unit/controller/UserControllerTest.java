package ru.rstdv.monitoringservice.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.exception.handler.ServiceExceptionHandler;
import ru.rstdv.monitoringservice.in.controller.UserController;
import ru.rstdv.monitoringservice.service.UserService;
import ru.rstdv.monitoringservice.service.UserServiceImpl;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UserControllerTest {
   private MockMvc mockMvc;

    private final Long ID = 1L;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        Mockito.mock(ServiceExceptionHandler.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @DisplayName("find by id should pass")
    @Test
    void findById_should_pass() throws Exception {
        var readUserDto = createReadUserDto();
        Mockito.doReturn(readUserDto).when(userService).findById(ID);

        mockMvc.perform(get("/api/v1/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }

    @DisplayName("find all")
    @Test
    void findAll() throws Exception {
        var readUserDto1 = createReadUserDto();
        var readUserDto2 = createReadUserDto();
        var readUserDto3 = createReadUserDto();

        Mockito.doReturn(List.of(readUserDto1, readUserDto2, readUserDto3)).when(userService).findAll();

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3));
    }


    private ReadUserDto createReadUserDto() {
        return new ReadUserDto(ID.toString(),
                "R",
                "test@gmail.com",
                Address.builder().build(),
                "USER",
                "000000000");
    }
}
