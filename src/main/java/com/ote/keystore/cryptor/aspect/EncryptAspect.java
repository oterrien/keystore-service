package com.ote.keystore.cryptor.aspect;

import com.ote.keystore.cryptor.service.CryptorService;
import com.ote.keystore.cryptor.annotation.Encrypt;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
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
    public void init(){
        log.warn("### EncryptAspect is loaded");
    }

    @Around("execution(* *(.., @com.ote.keystore.cryptor.annotation.Encrypt (*), ..))")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        Function<String, String> secretKeyValueProvider = new SecretKeyValueProvider(point).getValue();

        Object[] newParameters = IntStream.range(0, method.getParameters().length).
                mapToObj(i -> getParameter(method.getParameters()[i], point.getArgs()[i], secretKeyValueProvider)).
                collect(Collectors.toList()).
                toArray();

        return point.proceed(newParameters);
    }

    private Object getParameter(Parameter parameterToBeEncrypted, Object parameterToBeEncryptedValue, Function<String, String>  secretKeyValueProvider) {

        if (parameterToBeEncrypted.isAnnotationPresent(Encrypt.class)) {
            Encrypt encryptAnnotation = parameterToBeEncrypted.getAnnotation(Encrypt.class);
            String secretKeyParameter = encryptAnnotation.value();
            String secretKeyValue = secretKeyValueProvider.apply(secretKeyParameter);
            return cryptorService.encrypt(secretKeyValue, parameterToBeEncryptedValue);
        } else {
            return parameterToBeEncryptedValue;
        }
    }

    private static class SecretKeyValueProvider {

        @Getter
        private Function<String, String> value;

        private SecretKeyValueProvider(ProceedingJoinPoint point) {

            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            ExpressionParser parser = new SpelExpressionParser();
            MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(point.getTarget(), method, point.getArgs(), new ProceedingJoinPointParameterNameDiscoverer(point));

            this.value = ((expression) -> parser.parseExpression(expression).getValue(context, String.class));
        }
    }

    private static class ProceedingJoinPointParameterNameDiscoverer implements ParameterNameDiscoverer {

        private MethodSignature signature;

        private ProceedingJoinPointParameterNameDiscoverer(ProceedingJoinPoint point) {
            this.signature = (MethodSignature) point.getSignature();
        }

        @Override
        public String[] getParameterNames(Method method) {
            return this.signature.getParameterNames();
        }

        @Override
        public String[] getParameterNames(Constructor<?> constructor) {
            return this.signature.getParameterNames();
        }
    }
}