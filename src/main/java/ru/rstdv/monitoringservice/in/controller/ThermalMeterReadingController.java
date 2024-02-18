package ru.rstdv.monitoringservice.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.service.MeterReadingService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/thermal-meter-readings")
public class ThermalMeterReadingController {

    private final MeterReadingService<ReadThermalMeterReadingDto, CreateUpdateThermalMeterReadingDto> meterReadingService;

    @PostMapping
    public ResponseEntity<ReadThermalMeterReadingDto> create(@RequestBody @Validated CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto) {
        return ResponseEntity.status(CREATED)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.save(createUpdateThermalMeterReadingDto));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ReadThermalMeterReadingDto> findByMonthAndUserId(@PathVariable("id") Long id, @RequestParam("monthValue") Integer value) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.findByMonthAndUserId(new MonthFilterImpl(value), id));
    }

    @GetMapping("/actual/users/{id}")
    public ResponseEntity<ReadThermalMeterReadingDto> findActualByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.findActualByUserId(id));
    }

    @GetMapping("/all/users/{id}")
    public ResponseEntity<List<ReadThermalMeterReadingDto>> findAllByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(meterReadingService.findAllByUserId(id));
    }
}
