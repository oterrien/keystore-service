package com.ote.keystore.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This Aspect aims to decrypt each @Decrypted parameter
 */
@Profile("TestAspect")
@Component
@Slf4j
@Aspect
public class TestAspect {

    @PostConstruct
    public void init() {
        log.warn("### TestAspect is loaded");
    }

    @Around("execution(@com.ote.keystore.aop.TestAnnotation * * (..)))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        final Logger logger = LoggerFactory.getLogger(point.getTarget().getClass());
        logger.warn("########################## execute");

        Object result = point.proceed();

        if (result instanceof List) {
            Iterable<String> res = (Iterable<String>) result;
            return StreamSupport.stream(res.spliterator(), false).
                    map(p -> {
                        p = p.concat("_XXX");
                        return p;
                    }).collect(Collectors.toList());
        }

        if (result instanceof Page) {
            Iterable<TestService.Payload> res = (Iterable<TestService.Payload>) result;

            StreamSupport.stream(res.spliterator(), false).
                    forEach(p -> p.setValue(p.getValue().concat("_XXX")));

            return res;
        }

        if (result instanceof Map) {
            Map<Long, String> res = (Map<Long, String>) result;

            return res.entrySet().stream().map(p -> {
                p.setValue(p.getValue().concat("_XXX"));
                return p;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return result;
    }


}