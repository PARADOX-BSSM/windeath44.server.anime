package com.example.anime.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private static AtomicLong count = new AtomicLong(0);

    @Pointcut("execution(* com.example.anime.domain.anime.service.*.*(..))")
    public void animeServiceMethodLog() {}

    @Pointcut("execution(* com.example.anime.domain.character.service..*.*(..))")
    public void characterServiceMethodLog() {}

    @Around("animeServiceMethodLog() && characterServiceMethodLog()")
    public Object serviceMethodLooging(ProceedingJoinPoint joinPoint) throws Throwable {
        log.trace("Entering {} by {}", joinPoint.getSignature().getName(), count.get());
        Object result = joinPoint.proceed();
        log.trace("Exiting {} by {}", joinPoint.getSignature().getName(), count.getAndIncrement());
        return result;
    }
}
