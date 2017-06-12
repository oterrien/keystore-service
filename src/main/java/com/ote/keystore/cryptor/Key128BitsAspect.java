package com.ote.keystore.cryptor;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
public class Key128BitsAspect {

    @Around("execution(* *(.., @com.ote.keystore.cryptor.Key128Bits (*), ..))")
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
        if (parameter.isAnnotationPresent(Key128Bits.class) && parameter.getType().equals(String.class)) {
            return StringUtils.rightPad((String) parameterValue, 16, "X"); // 128 bits
        } else {
            return parameterValue;
        }
    }


}