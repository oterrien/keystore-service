package com.ote.keystore.cryptor.aspect;

import com.ote.keystore.cryptor.service.CryptorService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * This Aspect aims to decrypt each @Decrypted parameter
 */
@Component
@Slf4j
@Aspect
public class DecryptAspect {

    @Autowired
    private CryptorService cryptorService;

    @PostConstruct
    public void init() {
        log.warn("### DecryptAspect is loaded");
    }

    @Around("execution(@com.ote.keystore.cryptor.annotation.Decrypt * *(..)))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        return point.proceed();
    }


}