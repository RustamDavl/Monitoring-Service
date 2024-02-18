package ru.rstdv.monitoringservice.aspect;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.service.AuditService;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditableWaterMeterReadingServiceAspect {

    private final AuditService auditService;

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl.save(..))")
    public void annotatedByAuditableOnSave() {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl.findActualByUserId(..))")
    public void annotatedByAuditableOnFindActualByUserId() {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl.findAllByUserId(Long)) && args(id)")
    public void annotatedByAuditableOnFindAllByUserId(Long id) {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && " +
              "execution(* ru.rstdv.monitoringservice.service.WaterMeterReadingServiceImpl.findByMonthAndUserId(..))")
    public void annotatedByAuditableOnFindByMonthAndUserId() {
    }

    @AfterReturning(pointcut = "annotatedByAuditableOnSave()", returning = "readWaterMeterReadingDto")
    public void auditSave(Object readWaterMeterReadingDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadWaterMeterReadingDto) readWaterMeterReadingDto).userId(),
                AuditAction.WATER_METER_READING_SENDING.name(),
                LocalDateTime.now(),
                "water meter reading saved"
        ));
    }

    @AfterReturning(pointcut = "annotatedByAuditableOnFindActualByUserId()", returning = "readWaterMeterReadingDto")
    public void auditFindActualByUserId(Object readWaterMeterReadingDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadWaterMeterReadingDto) readWaterMeterReadingDto).userId(),
                AuditAction.GET_ACTUAL_WATER_METER_READING.name(),
                LocalDateTime.now(),
                "get actual result"
        ));
    }


    @Before(value = "annotatedByAuditableOnFindAllByUserId(id)", argNames = "id")
    public void beforeFindAllByUserId(Long id) {
        System.out.println("Before execution of findAllByUserId with id: " + id);
        auditService.saveAudit(new CreateAuditDto(
                id.toString(),
                AuditAction.GET_WATER_READING_HISTORY.name(),
                LocalDateTime.now(),
                "user got history of water meter reading"
        ));
    }


    @AfterReturning(pointcut = "annotatedByAuditableOnFindByMonthAndUserId()", returning = "readWaterMeterReadingDto")
    public void auditFindByMonthAndUserId(Object readWaterMeterReadingDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadWaterMeterReadingDto) readWaterMeterReadingDto).userId(),
                AuditAction.GET_WATER_READING_BY_MONTH.name(),
                LocalDateTime.now(),
                "user got water meter reading by month"
        ));
    }
}
