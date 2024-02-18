package ru.rstdv.monitoringservice.in.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;

@Loggable
@RestController
@RequestMapping("/api/v1/users/logout")
public class LogoutController {

    @PostMapping
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}
