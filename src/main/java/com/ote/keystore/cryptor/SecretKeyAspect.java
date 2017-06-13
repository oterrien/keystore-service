package com.ote.keystore.cryptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This Aspect aims to make @SecretKey parameters compatible with encrypting algorithm (number of bits)
 * The @SecretKey parameter is changed thanks to secretKeyService
 * Other parameters are not touched
 * Orign method is finally called with updated parameters
 */
@Component
@Slf4j
@Aspect
public class SecretKeyAspect {

    @Autowired
    private SecretKeyService secretKeyService;

    @Around("execution(* *(.., @com.ote.keystore.cryptor.SecretKey (*), ..))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        Object[] newParameters = IntStream.range(0, method.getParameters().length).
                mapToObj(i -> getParameter(method.getParameters()[i], point.getArgs()[i])).
                collect(Collectors.toList()).
                toArray();

        return point.proceed(newParameters);
    }

    private Object getParameter(Parameter parameter, Object parameterValue) {
        if (parameter.isAnnotationPresent(SecretKey.class)) {
            return secretKeyService.getSecretKey((String) parameterValue);
        } else {
            return parameterValue;
        }
    }
}