package ru.rstdv.monitoringservice.in.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.createupdate.UserAuthDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.service.UserService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
@Loggable
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRegistrationAuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ReadUserDto> register(@RequestBody @Validated CreateUpdateUserDto createUpdateUserDto) {
        return ResponseEntity.status(CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.register(createUpdateUserDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ReadUserDto> authenticate(@RequestBody @Validated UserAuthDto userAuthDto,
                                                    HttpServletRequest req, HttpServletResponse resp) {
        var session = req.getSession();
        var maybeUser = (ReadUserDto) session.getAttribute("user");
        if (maybeUser == null) {
            maybeUser = userService.authenticate(userAuthDto.email(), userAuthDto.password());
            session.setAttribute("user", maybeUser);
        }
        return ResponseEntity.status(OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(maybeUser);
    }
}
