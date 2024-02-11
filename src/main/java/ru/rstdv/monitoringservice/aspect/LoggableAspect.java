package ru.rstdv.monitoringservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggableAspect {

    @Pointcut("within(@ru.rstdv.monitoringservice.aspect.annotation.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method :" + proceedingJoinPoint.getSignature());
        long beginTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long res = endTime - beginTime;

        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() + " took " + res + " ms");
        return result;
    }
}
