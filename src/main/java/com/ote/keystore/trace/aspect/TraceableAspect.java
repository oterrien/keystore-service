package com.ote.keystore.trace.aspect;

import com.ote.keystore.trace.Counter;
import com.ote.keystore.trace.annotation.Traceable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

@Component
@Slf4j
@Aspect
public class TraceableAspect {

    @PostConstruct
    public void init(){
        log.warn("### TraceableAspect is loaded");
    }

    @Around("execution(@com.ote.keystore.trace.annotation.Traceable * *(..))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        final Logger logger = LoggerFactory.getLogger(point.getTarget().getClass());

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Traceable annotation = method.getAnnotation(Traceable.class);

        if (annotation.level().isEnabled(logger)) {
            return executeWithTrace(point, logger, annotation.level());
        } else {
            return point.proceed();
        }
    }

    private Object executeWithTrace(ProceedingJoinPoint point, Logger logger, Traceable.Level level) throws Throwable {
        long start = System.currentTimeMillis();
        long count = Counter.nextValue();

        try {
            level.log(logger, String.format("####-%d-%s- %s", count, StringUtils.rightPad("START", 5, " "), point.getSignature()));
            return point.proceed();
        } catch (Throwable e) {
            level.log(logger, String.format("####-%d-%s- %s", count, StringUtils.rightPad("ERROR", 5, " "), point.getSignature()), e);
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            level.log(logger, String.format("####-%d-%s- %s in %s ms", count, StringUtils.rightPad("END", 5, " "), point.getSignature(), (end - start)));
        }
    }
}