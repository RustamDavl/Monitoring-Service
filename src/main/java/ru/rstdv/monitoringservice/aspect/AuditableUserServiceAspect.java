package ru.rstdv.monitoringservice.aspect;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.service.AuditService;
import ru.rstdv.monitoringservice.service.UserService;

import java.time.LocalDateTime;

@Aspect
@RequiredArgsConstructor
@Component
public class AuditableUserServiceAspect {

    private final AuditService auditService;
    private final UserService userService;


    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && execution(* ru.rstdv.monitoringservice.service.UserService.register(..))")
    public void annotatedByAuditableOnRegister() {
    }

    @Pointcut("@annotation(ru.rstdv.monitoringservice.aspect.annotation.Auditable) && execution(* ru.rstdv.monitoringservice.service.UserService.authenticate(..))")
    public void annotatedByAuditableOnAuthenticate() {
    }

    @AfterReturning(pointcut = "annotatedByAuditableOnRegister()", returning = "readUserDto")
    public void auditRegister(Object readUserDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadUserDto) readUserDto).id(),
                AuditAction.REGISTRATION.name(),
                LocalDateTime.now(),
                "User registered successfully"
        ));
    }

    @AfterReturning(pointcut = "annotatedByAuditableOnAuthenticate()", returning = "readUserDto")
    public void auditAuthenticate(Object readUserDto) {
        auditService.saveAudit(new CreateAuditDto(
                ((ReadUserDto) readUserDto).id(),
                AuditAction.AUTHENTICATION.name(),
                LocalDateTime.now(),
                "User authenticated successfully"
        ));
    }
}
