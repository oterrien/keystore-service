package com.ote.keystore.cryptor.aspect;

import com.ote.keystore.cryptor.annotation.Decrypt;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * This Aspect aims to decrypt each time a @Decrypted annotation is specified for each method
 */
@Component
@Slf4j
@Aspect
public class DecryptAspect {

    @PostConstruct
    public void init() {
        log.warn("### DecryptAspect is loaded");
    }

    @Around("execution(@com.ote.keystore.cryptor.annotation.Decrypt * *(..)))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Decrypt decryptAnnotation = method.getAnnotation(Decrypt.class);

        Function<String, String> secretKeyValueProvider = new SecretKeyValueSpelParser(point).getValue();
        String secretKeyParameter = decryptAnnotation.secretKey();
        String secretKeyValue = secretKeyValueProvider.apply(secretKeyParameter);

        Decrypt.IDecrypter converter = decryptAnnotation.decrypter().getDeclaredConstructor(String.class).newInstance(secretKeyValue);
        Decrypt.IUpdater updater = decryptAnnotation.updater().newInstance();

        Object result = point.proceed();

        return result != null ? updater.update(result, converter) : null;
    }


}