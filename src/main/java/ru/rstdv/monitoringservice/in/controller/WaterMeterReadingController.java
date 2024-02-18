package ru.rstdv.monitoringservice.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.service.MeterReadingService;


import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/water-meter-readings")
public class WaterMeterReadingController {

    private final MeterReadingService<ReadWaterMeterReadingDto, CreateUpdateWaterMeterReadingDto> meterReadingService;

    @PostMapping
    public ResponseEntity<ReadWaterMeterReadingDto> create(@RequestBody @Validated CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto) {
        return ResponseEntity.status(CREATED)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.save(createUpdateWaterMeterReadingDto));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ReadWaterMeterReadingDto> findByMonthAndUserId(@PathVariable("id") Long id, @RequestParam("monthValue") Integer value) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.findByMonthAndUserId(new MonthFilterImpl(value), id));
    }

    @GetMapping("/actual/users/{id}")
    public ResponseEntity<ReadWaterMeterReadingDto> findActualByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.findActualByUserId(id));
    }

    @GetMapping("/all/users/{id}")
    public ResponseEntity<List<ReadWaterMeterReadingDto>> findAllByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.findAllByUserId(id));
    }
}
