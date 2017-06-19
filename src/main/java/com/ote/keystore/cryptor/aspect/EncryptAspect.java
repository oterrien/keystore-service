package com.ote.keystore.cryptor.aspect;

import com.ote.keystore.cryptor.Cryptable;
import com.ote.keystore.cryptor.annotation.Encrypt;
import com.ote.keystore.cryptor.service.CryptorService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This Aspect aims to encrypt each @Encrypted parameter
 */
@Component
@Slf4j
@Aspect
public class EncryptAspect {

    @Autowired
    private CryptorService cryptorService;

    @PostConstruct
    public void init() {
        log.warn("### EncryptAspect is loaded");
    }

    @Around("execution(* *(.., @com.ote.keystore.cryptor.annotation.Encrypt (*), ..))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        Function<String, String> secretKeyValueProvider = new SecretKeyValueSpelParser(point).getValue();

        Object[] newParameters = IntStream.range(0, method.getParameters().length).
                mapToObj(i -> getParameter(method.getParameters()[i], point.getArgs()[i], signature.getParameterNames()[i], secretKeyValueProvider)).
                collect(Collectors.toList()).
                toArray();

        return point.proceed(newParameters);
    }

    private Object getParameter(Parameter parameter, Object parameterValue, String parameterName, Function<String, String> secretKeyValueProvider) {

        if (parameter.isAnnotationPresent(Encrypt.class) && parameterValue != null) {
            log.trace("Trying to encrypt the parameter " + parameterName);
            if (parameterValue instanceof Cryptable) {
                Cryptable parameterToBeEncryptedValue = (Cryptable) parameterValue;
                if (parameterToBeEncryptedValue.isEncrypted()) {
                    return parameterValue;
                } else {
                    Encrypt encryptAnnotation = parameter.getAnnotation(Encrypt.class);
                    String secretKeyParameter = encryptAnnotation.secretKey();
                    String secretKeyValue = secretKeyValueProvider.apply(secretKeyParameter);
                    return cryptorService.encrypt(parameterToBeEncryptedValue, secretKeyValue);
                }
            } else {
                throw new CryptorService.EncryptException("Parameter " + parameterName + " has to implement " + Cryptable.class.getName());
            }
        } else {
            return parameterValue;
        }
    }
}