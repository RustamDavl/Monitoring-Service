package ru.rstdv.monitoringservice.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.service.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
@Loggable
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ReadUserDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReadUserDto>> findAll() {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(userService.findAll());
    }
}
