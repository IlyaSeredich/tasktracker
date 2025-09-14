package com.tasktracker.tasktrackerutil.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("execution(public * com.tasktracker.tasktrackeruserservice.controller.*.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logControllerMethod(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();

        logRequestStart(jp);

        Object result = jp.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        logRequestSuccess(jp, executionTime);
        return result;
    }

    private void logRequestStart(ProceedingJoinPoint jp) {
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            log.info("NEW REQUEST: IP: {}, URL: {}, HTTP_METHOD: {}, CONTROLLER_METHOD: {}.{}",
                    request.getRemoteAddr(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    jp.getSignature().getDeclaringTypeName(),
                    jp.getSignature().getName());
        }
    }

    private void logRequestSuccess(ProceedingJoinPoint jp, long executionTime) {
        log.info("Controller method {}.{} executed successfully in {}ms ",
                jp.getSignature().getDeclaringTypeName(),
                jp.getSignature().getName(),
                executionTime);
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        return request;
    }
}
