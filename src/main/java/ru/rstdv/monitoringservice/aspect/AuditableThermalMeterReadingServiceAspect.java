package ru.rstdv.monitoringservice.aspect;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.service.AuditService;

import java.time.LocalDateTime;


@Aspect
@RequiredArgsConstructor
@Component
public class AuditableThermalMeterReadingServiceAspect {

    private final AuditService auditService;


    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.ThermalMeterReadingServiceImpl.save(..))")
    public void annotatedByAuditableOnSave() {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.ThermalMeterReadingServiceImpl.findActualByUserId(..))")
    public void annotatedByAuditableOnFindActualByUserId() {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.ThermalMeterReadingServiceImpl.findAllByUserId(Long)) && args(id)")
    public void annotatedByAuditableOnFindAllByUserId(Long id) {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.ThermalMeterReadingServiceImpl.findByMonthAndUserId(..))")
    public void annotatedByAuditableOnFindByMonthAndUserId() {
    }

    @AfterReturning(pointcut = "annotatedByAuditableOnSave()", returning = "readThermalMeterReadingDto")
    public void auditSave(Object readThermalMeterReadingDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadThermalMeterReadingDto) readThermalMeterReadingDto).userId(),
                AuditAction.THERMAL_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "thermal meter reading saved"
        ));
    }

    @AfterReturning(pointcut = "annotatedByAuditableOnFindActualByUserId()", returning = "readThermalMeterReadingDto")
    public void auditFindActualByUserId(Object readThermalMeterReadingDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadThermalMeterReadingDto) readThermalMeterReadingDto).userId(),
                AuditAction.GET_ACTUAL_THERMAL_METER_READING.name(),
                LocalDateTime.now(),
                "get actual result"
        ));
    }


    @Before(value = "annotatedByAuditableOnFindAllByUserId(id)", argNames = "id")
    public void beforeFindAllByUserId(Long id) {
        System.out.println("Before execution of findAllByUserId with id: " + id);
        auditService.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.GET_THERMAL_READING_HISTORY.name(),
                LocalDateTime.now(),
                "user got history of thermal meter reading"
        ));
    }


    @AfterReturning(pointcut = "annotatedByAuditableOnFindByMonthAndUserId()", returning = "readThermalMeterReadingDto")
    public void auditFindByMonthAndUserId(Object readThermalMeterReadingDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadThermalMeterReadingDto) readThermalMeterReadingDto).userId(),
                AuditAction.GET_THERMAL_READING_BY_MONTH.name(),
                LocalDateTime.now(),
                "user got thermal meter reading by month"
        ));
    }
}
